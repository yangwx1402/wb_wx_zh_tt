var casper = require('casper').create({
    verbose: true,
    logLevel: 'error'
});
var fs = require('fs');
var cookie = 'UWSGI_GD=16e176a64a26786238ad77a4dce7a441;WEB2_APACHE2_JA=a17e0c9829243465552deacd7fa7aa03;WEB2_APACHE2_YZ=83db9e90979a7b5d5827a59af0d6bc0a;ULV=1484808885409:1:1:1:36.110.222.208_1484808884.569517:;SINAGLOBAL=36.110.222.208_1484732907.51084;zid=c7435c779331fcf94597e5ab7cf040a2;WEB3_QXG-01-TENGINE=d57ec8cf86a1832de61f85ca904c73c8;PHPSESSID=o6r5t1gl4ek4to53lipu01s5c6;sina_hy=1;UNID=D0DE6E240DF621588062B6;WEB3_PHP-FPM_GD=aab1bb3940e4d10a6205d02523134a24;tsc=3_588062b5_588062b5_0_1;a=Pc6Uc0EJ1S27;WEB3_PHP-FPM_GD=7bb7caf0d7764698432a843ad07c13c0;bdshare_firstime=1484808885037;Apache=36.110.222.208_1484808884.569517;UOR=,my.sina.com.cn,;tuijian=usrmdinst_2;SWB=usrmdinst_13;WEB2_APACHE2_YZ=ba9fd71dee934b4bc437519c31dad414;U_TRS2=000000d0.e1cc2a81.588062b4.b40fe0cc;U_TRS1=000000d0.e1be2a81.588062b4.0e6fe45f;ALF=1487400884;SSOLoginState=1484808884;ALF=1487400884;SSOLoginState=1484808884;SSOLoginState=1484808884;ALF=1516344883;SRF=1484808884;SRT=D.QqHBJZ4oTmHJTrMbPGYGS4uGiDEPdDYuW!SwTePHNEYdPqYBOQipMERt4EPKRcsrA4uJdFHaTsVuMDWQTevniGM9iQS-5OWtddjnAP0u4NsMiDSBI8t7*B.vAflW-P9Rc0lR-ykcDvnJqiQVbiRVPBtS!r3JZPQVqbgVdWiMZ4siOzu4DbmKPWFJQBZSCugdPPIUqzuNmV3SeESU-obi49ndDPIJcYPSrnlMc0k4ZEfOdibJCrr4rkiJcM1OFyHMPYJ5mkoODmkA!noJ8sJ5mklODEfi4noTDPJ5mjlOmHI5!noI4HJ5mkoODmkA!noJ8sJ5mkiODmkJ!noTmsr;SUHB=0qjkfLyo-5ME30;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WF1pcd.jYUKo29HG7fqYole5JpX5K2hUgL.Fo-R1hMce0-4SKM2dJLoIEXLxKqL1-BL12-LxKnL1h5L1h5LxK.LBKzL1-BLxKqL1-BL12-LxKML1-2L1hMt;SUB=_2A251hBLkDeTxGeNG41UX8yvFzjuIHXVW8AMsrDV8PUNbmtANLXb_kW9D7Wjp04ti5vvy7xYk0A_9Q-Q46A..;SCF=AmbO5ehmY32CgvBDwU8A_gTYug4g30JFWRGiu6XJ2fwBHebxpow5FFeW4mgjlE38Hxw6dsh5786JCRSHNoiEpf8.;cREMloginname=mhfq298270627%40163.com;sso_info=v02m6alo5qztKWRk5iljpSQpY6ElKWRk6CljoOApY6DgKWRk6SlkJOIpY6UkKWRk5iljpSQpY6ElKWRk5yljpSEpY6DnKadlqWkj5OUuI6DnLaMs5S5jZOcwA==;LT=1484808883;ALF=1516344883;ALC=ac%3D27%26bt%3D1484808883%26cv%3D5.0%26et%3D1516344883%26scf%3D%26uid%3D5887635957%26vf%3D0%26vs%3D0%26vt%3D0%26es%3Dab33753bbba015db2e752ce11c2ce3de;SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WF1pcd.jYUKo29HG7fqYole5NHD95Qf1hnNSoef1K-NWs4Dqcj_i--ciKL2iKy8i--RiKn7iKn7i--4i-2EiKL2i--ciKL2iKy8i--NiKLWiKnN;SUB=_2A251hBLjDeTxGeNG41UX8yvFzjuIHXVW8AMrrDV_PUNbm9ANLU7lkW-db_togAkNsS5Zo1_6n8XHatDqiw..;tgc=TGT-NTg4NzYzNTk1Nw==-1484808883-ja-A43FD71308C316D9A01266D5AAFAD09F-1;SCF=AmbO5ehmY32CgvBDwU8A_gTYug4g30JFWRGiu6XJ2fwBoERPRLfc2dNyT_-TAKqwgj-4jVFGmPZr-ufuVWGyooU.;ULOGIN_IMG=ja-9824dbe4ad28dfd4497659dec6ade251bbfa';
casper.userAgent('Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2');
cookie.split(";").forEach(function (pair) {
    pair = pair.split("=");
    console.log("-0000");
    phantom.addCookie({
        'name': pair[0],
        'value': pair[1],
        'domain': 's.weibo.com'
    });

});

console.log("add cookie end");

casper.start('http://s.weibo.com/weibo/%25E7%258E%258B%25E5%25A8%2581%25E7%258F%25AD%25E9%2595%25BF%25E5%25BE%25AE%25E5%258D%259A&Refer=focus_lx_STopic_box', function () {
   console.log("da kai wangye ")
});

casper.then(function () {
    this.wait(3000, function () {
        this.capture("./code_result.png");
    });
});
casper.run();