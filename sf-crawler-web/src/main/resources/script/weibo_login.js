//command=casperjs weibo_login.js keji8023367@163.com asd16811 ./img_code.png ./img_code.txt ./result.png ./cookie.txt
//command=casperjs weibo_login.js 13810108964 leshi123 ./img_code.png ./img_code.txt ./result.png ./cookie2.txt
var casper = require('casper').create({
    verbose: true,
    logLevel: 'error'
});
var fs = require('fs');
var args2 = casper.cli.args;
var username = args2.slice(0, 1);
var password = args2.slice(1, 2);
var codeimg = args2.slice(2, 3);
var codetxt = args2.slice(3, 4);
var resultimag = args2.slice(4, 5);
var cookiefile = args2.slice(5);
var codewait = 30000
console.log('username=' + username + ',password=' + password + ',codeimg=' + codeimg + ',codetxt=' + codetxt + ',resultimag=' + resultimag + ',cookiefile=' + cookiefile);
casper.userAgent('Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2');
casper.start('https://login.sina.com.cn/signup/signin.php', function () {
    this.fill('form#vForm', {
        'username': username,
        'password': password
    }, false);
});


casper.then(function () {
    this.click('input.W_btn_a.btn_34px');
    this.echo('login...');
    this.wait(1000, function () {
        if (this.exists('span[class="form_prompt"]')) {
            this.echo('登录失败，需要验证码:');
            this.wait(3000, function () {
                this.capture(codeimg);
                this.echo('获取验证码完成,验证码路经为:'+codeimg);
                this.echo('等待'+codewait+"毫秒进行验证码输入");
                this.wait(codewait, function () {
                    this.fill('form#vForm', {
                        'username': username,
                        'password': password,
                        'door': fs.read(codetxt),
                    }, false);
                    this.click('input.W_btn_a.btn_34px');
                    this.echo('login...');
                    this.wait(1000, function () {
                        this.echo('Login Successfully.');
                    });
                });

            });
        } else {
            this.echo('不需要验证码');
        }
    });
});

casper.then(function () {
    this.wait(3000, function () {
        this.capture(resultimag);
    });
});

casper.then(function () {
    var cookies = this.page.cookies;
    this.echo("cookie.length = " + cookies.length);
    var cookjson = JSON.stringify(phantom.cookies);
    this.echo("保存cookie到文件"+cookiefile);
    fs.write(cookiefile, cookjson);
});
casper.run();