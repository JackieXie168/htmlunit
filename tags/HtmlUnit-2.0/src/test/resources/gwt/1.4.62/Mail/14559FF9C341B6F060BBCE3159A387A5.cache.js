(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,BK='com.google.gwt.core.client.',CK='com.google.gwt.lang.',DK='com.google.gwt.sample.mail.client.',EK='com.google.gwt.user.client.',FK='com.google.gwt.user.client.impl.',aL='com.google.gwt.user.client.ui.',bL='com.google.gwt.user.client.ui.impl.',cL='java.lang.',dL='java.util.';function AK(){}
function fF(a){return this===a;}
function gF(){return CF(this);}
function dF(){}
_=dF.prototype={};_.eQ=fF;_.hC=gF;_.tN=cL+'Object';_.tI=1;function u(){return B();}
function v(a){return a==null?null:a.tN;}
var w=null;function z(a){return a==null?0:a.$H?a.$H:(a.$H=C());}
function A(a){return a==null?0:a.$H?a.$H:(a.$H=C());}
function B(){return $moduleBase;}
function C(){return ++D;}
var D=0;function ab(b,a){if(!yb(a,2)){return false;}return eb(b,xb(a,2));}
function bb(a){return z(a);}
function cb(){return [];}
function db(){return {};}
function fb(a){return ab(this,a);}
function eb(a,b){return a===b;}
function gb(){return bb(this);}
function E(){}
_=E.prototype=new dF();_.eQ=fb;_.hC=gb;_.tN=BK+'JavaScriptObject';_.tI=7;function ib(c,a,d,b,e){c.a=a;c.b=b;c.tN=e;c.tI=d;return c;}
function kb(a,b,c){return a[b]=c;}
function mb(a,b){return lb(a,b);}
function lb(a,b){return ib(new hb(),b,a.tI,a.b,a.tN);}
function nb(b,a){return b[a];}
function pb(b,a){return b[a];}
function ob(a){return a.length;}
function rb(e,d,c,b,a){return qb(e,d,c,b,0,ob(b),a);}
function qb(j,i,g,c,e,a,b){var d,f,h;if((f=nb(c,e))<0){throw new uE();}h=ib(new hb(),f,nb(i,e),nb(g,e),j);++e;if(e<a){j=sF(j,1);for(d=0;d<f;++d){kb(h,d,qb(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){kb(h,d,b);}}return h;}
function sb(f,e,c,g){var a,b,d;b=ob(g);d=ib(new hb(),b,e,c,f);for(a=0;a<b;++a){kb(d,a,pb(g,a));}return d;}
function tb(a,b,c){if(c!==null&&a.b!=0&& !yb(c,a.b)){throw new uD();}return kb(a,b,c);}
function hb(){}
_=hb.prototype=new dF();_.tN=CK+'Array';_.tI=8;function wb(b,a){return !(!(b&&Db[b][a]));}
function xb(b,a){if(b!=null)wb(b.tI,a)||Cb();return b;}
function yb(b,a){return b!=null&&wb(b.tI,a);}
function zb(a){return a&65535;}
function Ab(a){return ~(~a);}
function Bb(a){if(a>(lE(),mE))return lE(),mE;if(a<(lE(),nE))return lE(),nE;return a>=0?Math.floor(a):Math.ceil(a);}
function Cb(){throw new AD();}
function Eb(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var Db;function iA(b,a){zA(b.F(),a,true);}
function kA(a){return Eh(a.D());}
function lA(a){return Fh(a.D());}
function mA(a){return fi(a.r,'offsetHeight');}
function nA(a){return fi(a.r,'offsetWidth');}
function oA(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function pA(b,a){if(b.r!==null){oA(b,b.r,a);}b.r=a;}
function qA(b,c,a){b.ec(c);b.ac(a);}
function rA(b,a){yA(b.F(),a);}
function sA(b,a){Di(b.D(),a|hi(b.D()));}
function tA(){return this.r;}
function uA(){return this.r;}
function vA(a){return gi(a,'className');}
function wA(a){pA(this,a);}
function xA(a){Ci(this.r,'height',a);}
function yA(a,b){xi(a,'className',b);}
function zA(c,j,a){var b,d,e,f,g,h,i;if(c===null){throw iF(new hF(),'Null widget handle. If you are creating a composite, ensure that initWidget() has been called.');}j=uF(j);if(rF(j)==0){throw cE(new bE(),'Style names cannot be empty');}i=vA(c);e=pF(i,j);while(e!=(-1)){if(e==0||lF(i,e-1)==32){f=e+rF(j);g=rF(i);if(f==g||f<g&&lF(i,f)==32){break;}}e=qF(i,j,e+1);}if(a){if(e==(-1)){if(rF(i)>0){i+=' ';}xi(c,'className',i+j);}}else{if(e!=(-1)){b=uF(tF(i,0,e));d=uF(sF(i,e+rF(j)));if(rF(b)==0){h=d;}else if(rF(d)==0){h=b;}else{h=b+' '+d;}xi(c,'className',h);}}}
function AA(a,b){a.style.display=b?'':'none';}
function BA(a){AA(this.r,a);}
function CA(a){Ci(this.r,'width',a);}
function hA(){}
_=hA.prototype=new dF();_.D=tA;_.F=uA;_.Eb=wA;_.ac=xA;_.cc=BA;_.ec=CA;_.tN=aL+'UIObject';_.tI=11;_.r=null;function eC(a){if(!a.eb()){throw fE(new eE(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.vb();}finally{a.y();yi(a.D(),null);a.o=false;}}
function fC(a){if(yb(a.q,17)){xb(a.q,17).Bb(a);}else if(a.q!==null){throw fE(new eE(),"This widget's parent does not implement HasWidgets");}}
function gC(b,a){if(b.eb()){yi(b.D(),null);}pA(b,a);if(b.eb()){yi(a,b);}}
function hC(b,a){b.p=a;}
function iC(c,b){var a;a=c.q;if(b===null){if(a!==null&&a.eb()){c.mb();}c.q=null;}else{if(a!==null){throw fE(new eE(),'Cannot set a new parent without first clearing the old parent');}c.q=b;if(b.eb()){c.ib();}}}
function jC(){}
function kC(){}
function lC(){return this.o;}
function mC(){if(this.eb()){throw fE(new eE(),"Should only call onAttach when the widget is detached from the browser's document");}this.o=true;yi(this.D(),this);this.x();this.pb();}
function nC(a){}
function oC(){eC(this);}
function pC(){}
function qC(){}
function rC(a){gC(this,a);}
function fB(){}
_=fB.prototype=new hA();_.x=jC;_.y=kC;_.eb=lC;_.ib=mC;_.jb=nC;_.mb=oC;_.pb=pC;_.vb=qC;_.Eb=rC;_.tN=aL+'Widget';_.tI=12;_.o=false;_.p=null;_.q=null;function ov(b,a){iC(a,b);}
function qv(b,a){iC(a,null);}
function rv(){var a,b;for(b=this.fb();b.db();){a=xb(b.hb(),12);a.ib();}}
function sv(){var a,b;for(b=this.fb();b.db();){a=xb(b.hb(),12);a.mb();}}
function tv(){}
function uv(){}
function nv(){}
_=nv.prototype=new fB();_.x=rv;_.y=sv;_.pb=tv;_.vb=uv;_.tN=aL+'Panel';_.tI=13;function kx(a){lx(a,dh());return a;}
function lx(b,a){b.Eb(a);return b;}
function mx(a,b){if(a.n!==null){throw fE(new eE(),'SimplePanel can only contain one child widget');}a.dc(b);}
function ox(a,b){if(b===a.n){return;}if(b!==null){fC(b);}if(a.n!==null){a.Bb(a.n);}a.n=b;if(b!==null){ah(a.C(),a.n.D());ov(a,b);}}
function px(){return this.D();}
function qx(){return gx(new ex(),this);}
function rx(a){if(this.n!==a){return false;}qv(this,a);pi(this.C(),a.D());this.n=null;return true;}
function sx(a){ox(this,a);}
function dx(){}
_=dx.prototype=new nv();_.C=px;_.fb=qx;_.Bb=rx;_.dc=sx;_.tN=aL+'SimplePanel';_.tI=14;_.n=null;function Bv(){Bv=AK;jw=mD(new hD());}
function wv(a){Bv();lx(a,oD(jw));cw(a,0,0);return a;}
function xv(b,a){Bv();wv(b);b.g=a;return b;}
function yv(c,a,b){Bv();xv(c,a);c.k=b;return c;}
function zv(b,a){if(a.blur){a.blur();}}
function Av(c){var a,b,d;a=c.l;if(!a){dw(c,false);gw(c);}b=Bb((mk()-Dv(c))/2);d=Bb((lk()-Cv(c))/2);cw(c,nk()+b,ok()+d);if(!a){dw(c,true);}}
function Cv(a){return mA(a);}
function Dv(a){return nA(a);}
function Ev(a){Fv(a,false);}
function Fv(b,a){if(!b.l){return;}b.l=false;tm(zw(),b);b.D();}
function aw(a){var b;b=a.n;if(b!==null){if(a.h!==null){b.ac(a.h);}if(a.i!==null){b.ec(a.i);}}}
function bw(e,b){var a,c,d,f;d=Ah(b);c=mi(e.D(),d);f=Ch(b);switch(f){case 128:{a=e.ob(zb(xh(b)),zu(b));return a&&(c|| !e.k);}case 512:{a=(zb(xh(b)),zu(b),true);return a&&(c|| !e.k);}case 256:{a=(zb(xh(b)),zu(b),true);return a&&(c|| !e.k);}case 4:case 8:case 64:case 1:case 2:{if((Eg(),ri)!==null){return true;}if(!c&&e.g&&f==4){Fv(e,true);return true;}break;}case 2048:{if(e.k&& !c&&d!==null){zv(e,d);return false;}}}return !e.k||c;}
function cw(c,b,d){var a;if(b<0){b=0;}if(d<0){d=0;}c.j=b;c.m=d;a=c.D();Ci(a,'left',b+'px');Ci(a,'top',d+'px');}
function dw(a,b){Ci(a.D(),'visibility',b?'visible':'hidden');a.D();}
function ew(a,b){ox(a,b);aw(a);}
function fw(a,b){a.i=b;aw(a);if(rF(b)==0){a.i=null;}}
function gw(a){if(a.l){return;}a.l=true;Fg(a);Ci(a.D(),'position','absolute');if(a.m!=(-1)){cw(a,a.j,a.m);}rm(zw(),a);a.D();}
function hw(){return pD(jw,this.D());}
function iw(){return pD(jw,this.D());}
function kw(){qi(this);eC(this);}
function lw(a){return bw(this,a);}
function mw(a,b){return true;}
function nw(a){this.h=a;aw(this);if(rF(a)==0){this.h=null;}}
function ow(a){dw(this,a);}
function pw(a){ew(this,a);}
function qw(a){fw(this,a);}
function vv(){}
_=vv.prototype=new dx();_.C=hw;_.F=iw;_.mb=kw;_.nb=lw;_.ob=mw;_.ac=nw;_.cc=ow;_.dc=pw;_.ec=qw;_.tN=aL+'PopupPanel';_.tI=15;_.g=false;_.h=null;_.i=null;_.j=(-1);_.k=false;_.l=false;_.m=(-1);var jw;function xo(){xo=AK;Bv();}
function to(a){a.a=Es(new sq());a.f=aq(new Bp());}
function uo(a){xo();vo(a,false);return a;}
function vo(b,a){xo();wo(b,a,true);return b;}
function wo(c,a,b){xo();yv(c,a,b);to(c);As(c.f,0,0,c.a);c.f.ac('100%');ss(c.f,0);us(c.f,0);vs(c.f,0);er(c.f.b,1,0,'100%');hr(c.f.b,1,0,'100%');dr(c.f.b,1,0,(jt(),kt),(st(),tt));ew(c,c.f);rA(c,'gwt-DialogBox');rA(c.a,'Caption');Eu(c.a,c);return c;}
function yo(b,a){av(b.a,a);}
function zo(a,b){if(a.b!==null){rs(a.f,a.b);}if(b!==null){As(a.f,1,0,b);}a.b=b;}
function Ao(a){if(Ch(a)==4){if(mi(this.a.D(),Ah(a))){Dh(a);}}return bw(this,a);}
function Bo(a,b,c){this.e=true;ui(this.a.D());this.c=b;this.d=c;}
function Co(a){}
function Do(a){}
function Eo(c,d,e){var a,b;if(this.e){a=d+kA(this);b=e+lA(this);cw(this,a-this.c,b-this.d);}}
function Fo(a,b,c){this.e=false;oi(this.a.D());}
function ap(a){if(this.b!==a){return false;}rs(this.f,a);return true;}
function bp(a){zo(this,a);}
function cp(a){fw(this,a);this.f.ec('100%');}
function so(){}
_=so.prototype=new vv();_.nb=Ao;_.qb=Bo;_.rb=Co;_.sb=Do;_.tb=Eo;_.ub=Fo;_.Bb=ap;_.dc=bp;_.ec=cp;_.tN=aL+'DialogBox';_.tI=16;_.b=null;_.c=0;_.d=0;_.e=false;function hc(){hc=AK;xo();}
function gc(c){var a,b;hc();uo(c);yo(c,'About the Mail Sample');a=FA(new DA());b=Fs(new sq(),"This sample application demonstrates the construction of a complex user interface using GWT's built-in widgets.  Have a look at the code to see how easy it is to build your own apps!");rA(b,'mail-AboutText');aB(a,b);aB(a,Fm(new ym(),'Close',dc(new cc(),c)));zo(c,a);return c;}
function ic(a,b){switch(a){case 13:case 27:Ev(this);break;}return true;}
function bc(){}
_=bc.prototype=new so();_.ob=ic;_.tN=DK+'AboutDialog';_.tI=17;function dc(b,a){b.a=a;return b;}
function fc(a){Ev(this.a);}
function cc(){}
_=cc.prototype=new dF();_.lb=fc;_.tN=DK+'AboutDialog$1';_.tI=18;function mo(a){if(a.h===null){throw fE(new eE(),'initWidget() was never called in '+v(a));}return a.r;}
function no(a,b){if(a.h!==null){throw fE(new eE(),'Composite.initWidget() may only be called once.');}fC(b);a.Eb(b.D());a.h=b;iC(b,a);}
function oo(){return mo(this);}
function po(){if(this.h!==null){return this.h.eb();}return false;}
function qo(){this.h.ib();this.pb();}
function ro(){try{this.vb();}finally{this.h.mb();}}
function ko(){}
_=ko.prototype=new fB();_.D=oo;_.eb=po;_.ib=qo;_.mb=ro;_.tN=aL+'Composite';_.tI=19;_.h=null;function vc(a){a.a=sb('[Lcom.google.gwt.sample.mail.client.Contacts$Contact;',126,22,[sc(new oc(),'Benoit Mandelbrot','benoit@example.com',a),sc(new oc(),'Albert Einstein','albert@example.com',a),sc(new oc(),'Rene Descartes','rene@example.com',a),sc(new oc(),'Bob Saget','bob@example.com',a),sc(new oc(),'Ludwig von Beethoven','ludwig@example.com',a),sc(new oc(),'Richard Feynman','richard@example.com',a),sc(new oc(),'Alan Turing','alan@example.com',a),sc(new oc(),'John von Neumann','john@example.com',a)]);a.b=FA(new DA());}
function wc(d,b){var a,c;vc(d);c=kx(new dx());c.dc(d.b);for(a=0;a<d.a.a;++a){xc(d,d.a[a]);}no(d,c);rA(d,'mail-Contacts');return d;}
function xc(c,a){var b;b=Fs(new sq(),"<a href='javascript:;'>"+a.b+'<\/a>');aB(c.b,b);Du(b,lc(new kc(),c,a,b));}
function jc(){}
_=jc.prototype=new ko();_.tN=DK+'Contacts';_.tI=20;function lc(b,a,c,d){b.a=a;b.b=c;b.c=d;return b;}
function nc(c){var a,b,d;b=qc(new pc(),this.b,this.a);a=kA(this.c)+14;d=lA(this.c)+14;cw(b,a,d);gw(b);}
function kc(){}
_=kc.prototype=new dF();_.lb=nc;_.tN=DK+'Contacts$1';_.tI=21;function sc(d,b,a,c){d.b=b;d.a=a;return d;}
function oc(){}
_=oc.prototype=new dF();_.tN=DK+'Contacts$Contact';_.tI=22;_.a=null;_.b=null;function rc(){rc=AK;Bv();}
function qc(g,a,f){var b,c,d,e;rc();xv(g,true);d=FA(new DA());e=Cu(new Au(),a.b);b=Cu(new Au(),a.a);aB(d,e);aB(d,b);c=zt(new xt());jn(c,4);At(c,BC((ie(),le)));At(c,d);mx(g,c);rA(g,'mail-ContactPopup');rA(e,'mail-ContactPopupName');rA(b,'mail-ContactPopupEmail');return g;}
function pc(){}
_=pc.prototype=new vv();_.tN=DK+'Contacts$ContactPopup';_.tI=23;function ce(){ce=AK;Ae=he(new ge());}
function ae(a){a.e=yf(new vf(),Ae);a.c=FA(new DA());a.a=bd(new Fc());a.d=lf(new df(),Ae);}
function be(a){ce();ae(a);return a;}
function de(b,a){ed(b.a,a);}
function ee(b){var a;Ce=b;b.e.ec('100%');b.b=yd(new wd());b.b.ec('100%');aB(b.c,b.b);aB(b.c,b.a);b.b.ec('100%');b.a.ec('100%');a=mp(new dp());np(a,b.e,(op(),wp));np(a,b.d,(op(),yp));np(a,b.c,(op(),up));a.ec('100%');jn(a,4);tp(a,b.c,'100%');ek(b);hk(false);uk('0px');rm(zw(),a);cj(Bc(new Ac(),b));fe(b,mk(),lk());}
function fe(c,d,a){var b;b=a-lA(c.d)-8;if(b<1){b=1;}c.d.ac(''+b);cd(c.a,d,a);}
function Be(b,a){fe(this,b,a);}
function zc(){}
_=zc.prototype=new dF();_.yb=Be;_.tN=DK+'Mail';_.tI=24;_.b=null;var Ae,Ce=null;function Bc(b,a){b.a=a;return b;}
function Dc(){fe(this.a,mk(),lk());}
function Ac(){}
_=Ac.prototype=new dF();_.A=Dc;_.tN=DK+'Mail$1';_.tI=25;function ad(a){a.c=FA(new DA());a.b=FA(new DA());a.g=Es(new sq());a.f=Es(new sq());a.d=Es(new sq());a.a=Es(new sq());a.e=Fw(new Dw(),a.a);}
function bd(b){var a;ad(b);bv(b.a,true);aB(b.b,b.g);aB(b.b,b.f);aB(b.b,b.d);b.b.ec('100%');a=mp(new dp());np(a,b.b,(op(),wp));np(a,b.e,(op(),up));qp(a,b.e,'100%');aB(b.c,a);qA(a,'100%','100%');qA(b.e,'100%','100%');no(b,b.c);rA(b,'mail-Detail');rA(b.b,'mail-DetailHeader');rA(a,'mail-DetailInner');rA(b.g,'mail-DetailSubject');rA(b.f,'mail-DetailSender');rA(b.d,'mail-DetailRecipient');rA(b.a,'mail-DetailBody');return b;}
function cd(c,e,d){var a,b;b=e-kA(c.e)-9;if(b<1){b=1;}a=d-lA(c.e)-9;if(a<1){a=1;}qA(c.e,''+b,''+a);}
function ed(b,a){ct(b.g,a.e);ct(b.f,'<b>From:<\/b>&nbsp;'+a.d);ct(b.d,'<b>To:<\/b>&nbsp;foo@example.com');ct(b.a,a.a);}
function Fc(){}
_=Fc.prototype=new ko();_.tN=DK+'MailDetail';_.tI=26;function gd(e,c,b,d,a){e.d=c;e.b=b;e.e=d;e.a=a;return e;}
function fd(){}
_=fd.prototype=new dF();_.tN=DK+'MailItem';_.tI=27;_.a=null;_.b=null;_.c=false;_.d=null;_.e=null;function jd(){jd=AK;var a;td=sb('[Ljava.lang.String;',127,1,['markboland05','Hollie Voss','boticario','Emerson Milton','Healy Colette','Brigitte Cobb','Elba Lockhart','Claudio Engle','Dena Pacheco','Brasil s.p','Parker','derbvktqsr','qetlyxxogg','antenas.sul','Christina Blake','Gail Horton','Orville Daniel','PostMaster','Rae Childers','Buster misjenou','user31065','ftsgeolbx','aqlovikigd','user18411','Mildred Starnes','Candice Carson','Louise Kelchner','Emilio Hutchinson','Geneva Underwood','Residence Oper?','fpnztbwag','tiger','Heriberto Rush','bulrush Bouchard','Abigail Louis','Chad Andrews','bjjycpaa','Terry English','Bell Snedden','huang','hhh','(unknown sender)','Kent','Dirk Newman','Equipe Virtual Cards','wishesundmore','Benito Meeks']);md=sb('[Ljava.lang.String;',127,1,['mark@example.com','hollie@example.com','boticario@example.com','emerson@example.com','healy@example.com','brigitte@example.com','elba@example.com','claudio@example.com','dena@example.com','brasilsp@example.com','parker@example.com','derbvktqsr@example.com','qetlyxxogg@example.com','antenas_sul@example.com','cblake@example.com','gailh@example.com','orville@example.com','post_master@example.com','rchilders@example.com','buster@example.com','user31065@example.com','ftsgeolbx@example.com','aqlovikigd@example.com','user18411@example.com','mildred@example.com','candice@example.com','louise_kelchner@example.com','emilio@example.com','geneva@example.com','residence_oper@example.com','fpnztbwag@example.com','tiger@example.com','heriberto@example.com','bulrush@example.com','abigail_louis@example.com','chada@example.com','bjjycpaa@example.com','terry@example.com','bell@example.com','huang@example.com','hhh@example.com','kent@example.com','newman@example.com','equipe_virtual@example.com','wishesundmore@example.com','benito@example.com']);vd=sb('[Ljava.lang.String;',127,1,['URGENT -[Mon, 24 Apr 2006 02:17:27 +0000]','URGENT TRANSACTION -[Sun, 23 Apr 2006 13:10:03 +0000]','fw: Here it comes','voce ganho um vale presente Boticario','Read this ASAP','Hot Stock Talk','New Breed of Equity Trader','FWD: TopWeeks the wire special pr news release','[fwd] Read this ASAP','Renda Extra R$1.000,00-R$2.000,00/m?s','re: Make sure your special pr news released','Forbidden Knowledge Conference','decodificadores os menores pre?os','re: Our Pick','RE: The hottest pick Watcher','RE: St0kkMarrkett Picks Trade watch special pr news release','St0kkMarrkett Picks Watch special pr news release news','You are a Winner oskoxmshco','Encrypted E-mail System (VIRUS REMOVED)','Fw: Malcolm','Secure Message System (VIRUS REMOVED)','fwd: St0kkMarrkett Picks Watch special pr news releaser','FWD: Financial Market Traderr special pr news release','? s? uma dica r?pida !!!!! leia !!!','re: You have to heard this','fwd: Watcher TopNews','VACANZE alle Mauritius','funny','re: You need to review this','[re:] Our Pick','RE: Before the be11 special pr news release','[re:] Market TradePicks Trade watch news','No prescription needed','Seu novo site','[fwd] Financial Market Trader Picker','FWD: Top Financial Market Specialists Trader interest increases','Os cart?es mais animados da web!!','We will sale 4 you cebtdbwtcv','RE: Best Top Financial Market Specialists Trader Picks']);od=sb('[Ljava.lang.String;',127,1,['Dear Friend,<br><br>I am Mr. Mark Boland the Bank Manager of ABN AMRO BANK 101 Moorgate, London, EC2M 6SB.<br><br>','I have an urgent and very confidential business proposition for you. On July 20, 2001; Mr. Zemenu Gente, a National of France, who used to be a private contractor with the Shell Petroleum Development Company in Saudi Arabia. Mr. Zemenu Gente Made a Numbered time (Fixed deposit) for 36 calendar months, valued at GBP?30, 000,000.00 (Thirty Million Pounds only) in my Branch.','I have all necessary legal documents that can be used to back up any claim we may make. All I require is your honest Co-operation, Confidentiality and A trust to enable us sees this transaction through. I guarantee you that this will be executed under a legitimate arrangement that will protect you from any breach of the law. Please get in touch with me urgently by E-mail and Provide me with the following;<br>','The OIL sector is going crazy. This is our weekly gift to you!<br><br>Get KKPT First Thing, This Is Going To Run!<br><br>Check out Latest NEWS!<br><br>KOKO PETROLEUM (KKPT) - This is our #1 pick for next week!<br>Our last pick gained $2.16 in 4 days of trading.<br>','LAS VEGAS, NEVADA--(MARKET WIRE)--Apr 6, 2006 -- KOKO Petroleum, Inc. (Other OTC:KKPT.PK - News) -<br>KOKO Petroleum, Inc. announced today that its operator for the Corsicana Field, JMT Resources, Ltd. ("JMT") will commence a re-work program on its Pecan Gap wells in the next week. The re-work program will consist of drilling six lateral bore production strings from the existing well bore. This process, known as Radial Jet Enhancement, will utilize high pressure fluids to drill the lateral well bores, which will extend out approximately 350\' each.','JMT has contracted with Well Enhancement Services, LLC (www.wellenhancement.com) to perform the rework on its Pierce nos. 14 and 14a. A small sand frac will follow the drilling of the lateral well bores in order to enhance permeability and create larger access to the Pecan Gap reservoir. Total cost of the re-work per well is estimated to be approximately $50,000 USD.','Parab?ns!<br>Voc? Ganhou Um Vale Presente da Botic?rio no valor de R$50,00<br>Voc? foi contemplado na Promo??o Respeite Minha Natureza - Pulseira Social.<br>Algu?m pode t?-lo inscrito na promo??o! (Amigos(as), Namorado(a) etc.).<br>Para retirar o seu pr?mio em uma das nossas Lojas, fa?a o download do Vale-Presente abaixo.<br>Ap?s o download, com o arquivo previamente salvo, imprima uma folha e salve a c?pia em seu computador para evitar transtornos decorrentes da perda do mesmo. Lembramos que o Vale-Presente ? ?nico e intransfer?vel.','Large Marketing Campaign running this weekend!<br><br>Should you get in today before it explodes?<br><br>This Will Fly Starting Monday!','PREMIER INFORMATION (PIFR)<br>A U.S. based company offers specialized information management serices to both the Insurance and Healthcare Industries. The services we provide are specific to each industry and designed for quick response and maximum security.<br><br>STK- PIFR<br>Current Price: .20<br>This one went to $2.80 during the last marketing Campaign!','These partnerships specifically allow Premier to obtain personal health information, as governed by the Health In-surancee Portability and Accountability Act of 1996 (HIPAA), and other applicable state laws and regulations.<br><br>Global HealthCare Market Undergoing Digital Conversion','>>   Componentes e decodificadores; confira aqui;<br> http://br.geocities.com/listajohn/index.htm<br>','THE GOVERNING AWARD<br>NETHERLANDS HEAD OFFICE<br>AC 76892 HAUITSOP<br>AMSTERDAM, THE NETHERLANDS.<br>FROM: THE DESK OF THE PROMOTIONS MANAGER.<br>INTERNATIONAL PROMOTIONS / PRIZE AWARD DEPARTMENT<br>REF NUMBER: 14235/089.<br>BATCH NUMBER: 304/64780/IFY.<br>RE/AWARD NOTIFICATION<br>',"We are pleased to inform you of the announcement today 13th of April 2006, you among TWO LUCKY WINNERS WON the GOVERNING AWARD draw held on the 28th of March 2006. The THREE Winning Addresses were randomly selected from a batch of 10,000,000 international email addresses. Your email address emerged alongside TWO others as a category B winner in this year's Annual GOVERNING AWARD Draw.<br>",'>> obrigado por me dar esta pequena aten??o !!!<br>CASO GOSTE DE ASSISTIR TV , MAS A SUA ANTENA S? PEGA AQUELES CANAIS LOCAIS  OU O SEU SISTEMA PAGO ? MUITO CARO , SAIBA QUE TENHO CART?ES DE ACESSO PARA SKY DIRECTV , E DECODERS PARA  NET TVA E TECSAT , TUDO GRATIS , SEM ASSINTURA , SEM MENSALIDADE, VC PAGA UMA VEZ S? E ASSISTE A MUITOS CANAIS , FILMES , JOGOS , PORNOS , DESENHOS , DOCUMENT?RIOS ,SHOWS , ETC,<br><br>CART?O SKY E DIRECTV TOTALMENTE HACKEADOS  350,00<br>DECODERS NET TVA DESBLOQUEADOS                       390,00<br>KITS COMPLETOS SKY OU DTV ANTENA DECODER E CART?O  650,00<br>TECSAT FREE   450,00<br>TENHO TB ACESS?RIOS , CABOS, LNB .<br>','********************************************************************<br> Original filename: mail.zip<br> Virus discovered: JS.Feebs.AC<br>********************************************************************<br> A file that was attached to this email contained a virus.<br> It is very likely that the original message was generated<br> by the virus and not a person - treat this message as you would<br> any other junk mail (spam).<br> For more information on why you received this message please visit:<br>','Put a few letters after your name. Let us show you how you can do it in just a few days.<br><br>http://thewrongchoiceforyou.info<br><br>kill future mailing by pressing this : see main website',"We possess scores of pharmaceutical products handy<br>All med's are made in U.S. laboratories<br>For your wellbeing! Very rapid, protected and secure<br>Ordering, No script required. We have the pain aid you require<br>",'"Oh, don\'t speak to me of Austria. Perhaps I don\'t understand things, but Austria never has wished, and does not wish, for war. She is betraying us! Russia alone must save Europe. Our gracious sovereign recognizes his high vocation and will be true to it. That is the one thing I have faith in! Our good and wonderful sovereign has to perform the noblest role on earth, and he is so virtuous and noble that God will not forsake him. He will fulfill his vocation and crush the hydra of revolution, which has become more terrible than ever in the person of this murderer and villain! We alone must avenge the blood of the just one.... Whom, I ask you, can we rely on?... England with her commercial spirit will not and cannot understand the Emperor Alexander\'s loftiness of soul. She has refused to evacuate Malta. She wanted to find, and still seeks, some secret motive in our actions. What answer did Novosiltsev get? None. The English have not understood and cannot understand the self-ab!<br>negation of our Emperor who wants nothing for himself, but only desires the good of mankind. And what have they promised? Nothing! And what little they have promised they will not perform! Prussia has always declared that Buonaparte is invincible, and that all Europe is powerless before him.... And I don\'t believe a word that Hardenburg says, or Haugwitz either. This famous Prussian neutrality is just a trap. I have faith only in God and the lofty destiny of our adored monarch. He will save Europe!"<br>"Those were extremes, no doubt, but they are not what is most important. What is important are the rights of man, emancipation from prejudices, and equality of citizenship, and all these ideas Napoleon has retained in full force."']);rd=iI(new gI());{for(a=0;a<37;++a){jI(rd,kd());}}}
function kd(){jd();var a,b,c,d,e;d=td[sd++];if(sd==td.a){sd=0;}b=md[ld++];if(ld==md.a){ld=0;}e=vd[ud++];if(ud==vd.a){ud=0;}a='';for(c=0;c<10;++c){a+=od[nd++];if(nd==od.a){nd=0;}}return gd(new fd(),d,b,e,a);}
function qd(a){jd();if(a>=rd.b){return null;}return xb(nI(rd,a),4);}
function pd(){jd();return rd.b;}
var ld=0,md,nd=0,od,rd,sd=0,td,ud=0,vd;function xd(a){a.a=Es(new sq());a.c=at(new sq(),"<a href='javascript:;'>&lt; newer<\/a>",true);a.d=at(new sq(),"<a href='javascript:;'>older &gt;<\/a>",true);a.g=aq(new Bp());a.b=zt(new xt());}
function yd(b){var a;xd(b);vs(b.g,0);us(b.g,0);b.g.ec('100%');gs(b.g,b);Du(b.c,b);Du(b.d,b);a=zt(new xt());rA(b.b,'mail-ListNavBar');At(a,b.c);At(a,b.a);At(a,b.d);Dt(b.b,(jt(),mt));At(b.b,a);b.b.ec('100%');no(b,b.g);rA(b,'mail-List');Ad(b);Dd(b);return b;}
function Ad(b){var a;zs(b.g,0,0,'Sender');zs(b.g,0,1,'Email');zs(b.g,0,2,'Subject');As(b.g,0,3,b.b);ur(b.g.d,0,'mail-ListHeader');for(a=0;a<10;++a){zs(b.g,a+1,0,'');zs(b.g,a+1,1,'');zs(b.g,a+1,2,'');ir(b.g.b,a+1,0,false);ir(b.g.b,a+1,1,false);ir(b.g.b,a+1,2,false);Fp(b.g.b,a+1,2,2);}}
function Bd(c,b){var a;a=qd(c.f+b);if(a===null){return;}Cd(c,c.e,false);Cd(c,b,true);a.c=true;c.e=b;de((ce(),Ce),a);}
function Cd(c,a,b){if(a!=(-1)){if(b){pr(c.g.d,a+1,'mail-SelectedRow');}else{tr(c.g.d,a+1,'mail-SelectedRow');}}}
function Dd(e){var a,b,c,d;a=pd();d=e.f+10;if(d>a){d=a;}e.c.cc(e.f!=0);e.d.cc(e.f+10<a);av(e.a,''+(e.f+1)+' - '+d+' of '+a);b=0;for(;b<10;++b){if(e.f+b>=pd()){break;}c=qd(e.f+b);zs(e.g,b+1,0,c.d);zs(e.g,b+1,1,c.b);zs(e.g,b+1,2,c.e);}for(;b<10;++b){xs(e.g,b+1,0,'&nbsp;');xs(e.g,b+1,1,'&nbsp;');xs(e.g,b+1,2,'&nbsp;');}if(e.e==(-1)){Bd(e,0);}}
function Ed(c,b,a){if(b>0){Bd(this,b-1);}}
function Fd(a){if(a===this.d){this.f+=10;if(this.f>=pd()){this.f-=10;}else{Cd(this,this.e,false);this.e=(-1);Dd(this);}}else if(a===this.c){this.f-=10;if(this.f<0){this.f=0;}else{Cd(this,this.e,false);this.e=(-1);Dd(this);}}}
function wd(){}
_=wd.prototype=new ko();_.kb=Ed;_.lb=Fd;_.tN=DK+'MailList';_.tI=28;_.e=(-1);_.f=0;function ie(){ie=AK;je=u()+'B9DA8B0768BAD7283674A8E1D92AD03D.cache.png';ke=yC(new xC(),je,0,0,32,32);le=yC(new xC(),je,32,0,32,32);me=yC(new xC(),je,64,0,16,16);ne=yC(new xC(),je,80,0,16,16);oe=yC(new xC(),je,96,0,16,16);pe=yC(new xC(),je,112,0,4,4);qe=yC(new xC(),je,116,0,140,75);re=yC(new xC(),je,256,0,32,32);se=yC(new xC(),je,288,0,4,4);te=yC(new xC(),je,292,0,16,16);ue=yC(new xC(),je,308,0,32,32);ve=yC(new xC(),je,340,0,16,16);we=yC(new xC(),je,356,0,16,16);xe=yC(new xC(),je,372,0,16,16);ye=yC(new xC(),je,388,0,16,16);ze=yC(new xC(),je,404,0,16,16);}
function he(a){ie();return a;}
function ge(){}
_=ge.prototype=new dF();_.tN=DK+'Mail_Images_generatedBundle';_.tI=29;var je,ke,le,me,ne,oe,pe,qe,re,se,te,ue,ve,we,xe,ye,ze;function Fe(c,a){var b;c.a=qz(new ry(),a);b=Dy(new Ay(),cf(c,(ie(),ne),'foo@example.com'));rz(c.a,b);af(c,b,'Inbox',(ie(),oe));af(c,b,'Drafts',(ie(),me));af(c,b,'Templates',(ie(),ve));af(c,b,'Sent',(ie(),te));af(c,b,'Trash',(ie(),we));hz(b,true);no(c,c.a);return c;}
function af(d,c,e,a){var b;b=Dy(new Ay(),cf(d,a,e));c.s(b);return b;}
function cf(b,a,c){return '<span>'+CC(a)+c+'<\/span>';}
function De(){}
_=De.prototype=new ko();_.tN=DK+'Mailboxes';_.tI=30;_.a=null;function kf(a){a.b=ff(new ef(),a);}
function lf(b,a){kf(b);mf(b,a,Fe(new De(),a),(ie(),re),'Mail');mf(b,a,tf(new sf()),(ie(),ue),'Tasks');mf(b,a,wc(new jc(),a),(ie(),ke),'Contacts');no(b,b.b);return b;}
function mf(d,c,e,b,a){iA(e,'mail-StackContent');ay(d.b,e,pf(d,c,b,a),true);}
function of(b,a){return 'header-'+b.hC()+'-'+a;}
function pf(g,e,d,a){var b,c,f;f=g.a==0;c=of(g,g.a);g.a++;b="<table class='caption' cellpadding='0' cellspacing='0'><tr><td class='lcaption'>"+CC(d)+"<\/td><td class='rcaption'><b style='white-space:nowrap'>"+a+'<\/b><\/td><\/tr><\/table>';return "<table id='"+c+"' align='left' cellpadding='0' cellspacing='0'"+(f?" class='is-top'":'')+'><tbody>'+"<tr><td class='box-00'>"+CC((ie(),pe))+'<\/td>'+"<td class='box-10'>&nbsp;<\/td>"+"<td class='box-20'>"+CC((ie(),se))+'<\/td>'+'<\/tr><tr>'+"<td class='box-01'>&nbsp;<\/td>"+"<td class='box-11'>"+b+'<\/td>'+"<td class='box-21'>&nbsp;<\/td>"+'<\/tr><\/tbody><\/table>';}
function qf(d,c,b){var a;c++;if(c>0&&c<d.b.f.b){a=di(of(d,c));xi(a,'className','');}b++;if(b>0&&b<d.b.f.b){a=di(of(d,b));xi(a,'className','is-beneath-selected');}}
function rf(){iy(this.b,0);qf(this,(-1),0);}
function df(){}
_=df.prototype=new ko();_.pb=rf;_.tN=DK+'Shortcuts';_.tI=31;_.a=0;function Dn(a){a.f=oB(new gB(),a);}
function En(a){Dn(a);return a;}
function Fn(c,a,b){fC(a);pB(c.f,a);ah(b,a.D());ov(c,a);}
function ao(d,b,a){var c;bo(d,a);if(b.q===d){c=eo(d,b);if(c<a){a--;}}return a;}
function bo(b,a){if(a<0||a>b.f.b){throw new hE();}}
function fo(b,a){return rB(b.f,a);}
function eo(b,a){return sB(b.f,a);}
function go(e,b,c,a,d){a=ao(e,b,a);fC(b);tB(e.f,b,a);if(d){li(c,mo(b),a);}else{ah(c,mo(b));}ov(e,b);}
function ho(b,c){var a;if(c.q!==b){return false;}qv(b,c);a=c.D();pi(ji(a),a);wB(b.f,c);return true;}
function io(){return uB(this.f);}
function jo(a){return ho(this,a);}
function Cn(){}
_=Cn.prototype=new nv();_.fb=io;_.Bb=jo;_.tN=aL+'ComplexPanel';_.tI=32;function Ex(b){var a;En(b);a=mh();b.Eb(a);b.b=jh();ah(a,b.b);wi(a,'cellSpacing',0);wi(a,'cellPadding',0);Di(a,1);rA(b,'gwt-StackPanel');return b;}
function Fx(a,b){dy(a,b,a.f.b);}
function ay(c,d,b,a){Fx(c,d);gy(c,c.f.b-1,b,a);}
function cy(d,a){var b,c;while(a!==null&& !bh(a,d.D())){b=gi(a,'__index');if(b!==null){c=fi(a,'__owner');if(c==d.hC()){return oE(b);}else{return (-1);}}a=ji(a);}return (-1);}
function dy(e,h,a){var b,c,d,f,g;g=lh();d=kh();ah(g,d);f=lh();c=kh();ah(f,c);a=ao(e,h,a);b=a*2;li(e.b,f,b);li(e.b,g,b);zA(d,'gwt-StackPanelItem',true);wi(d,'__owner',e.hC());xi(d,'height','1px');xi(c,'height','100%');xi(c,'vAlign','top');go(e,h,c,a,false);jy(e,a);if(e.c==(-1)){iy(e,0);}else{hy(e,a,false);if(e.c>=a){++e.c;}}}
function ey(d,a){var b,c;if(Ch(a)==1){c=Ah(a);b=cy(d,c);if(b!=(-1)){iy(d,b);}}}
function fy(e,a,b){var c,d,f;c=ho(e,a);if(c){d=2*b;f=ci(e.b,d);pi(e.b,f);f=ci(e.b,d);pi(e.b,f);if(e.c==b){e.c=(-1);}else if(e.c>b){--e.c;}jy(e,d);}return c;}
function gy(e,b,d,a){var c;if(b>=e.f.b){return;}c=ci(ci(e.b,b*2),0);if(a){zi(c,d);}else{Ai(c,d);}}
function hy(c,a,e){var b,d;d=ci(c.b,a*2);if(d===null){return;}b=ii(d);zA(b,'gwt-StackPanelItem-selected',e);d=ci(c.b,a*2+1);AA(d,e);fo(c,a).cc(e);}
function iy(b,a){if(a>=b.f.b||a==b.c){return;}if(b.c>=0){hy(b,b.c,false);}b.c=a;hy(b,b.c,true);}
function jy(f,a){var b,c,d,e;for(e=a,b=f.f.b;e<b;++e){d=ci(f.b,e*2);c=ii(d);wi(c,'__index',e);}}
function ky(a){ey(this,a);}
function ly(a){return fy(this,a,eo(this,a));}
function Dx(){}
_=Dx.prototype=new Cn();_.jb=ky;_.Bb=ly;_.tN=aL+'StackPanel';_.tI=33;_.b=null;_.c=(-1);function ff(b,a){b.a=a;Ex(b);return b;}
function hf(a){var b,c;c=this.c;ey(this,a);b=this.c;if(c!=b){qf(this.a,c,b);}}
function ef(){}
_=ef.prototype=new Dx();_.jb=hf;_.tN=DK+'Shortcuts$1';_.tI=34;function tf(c){var a,b;b=kx(new dx());a=FA(new DA());b.dc(a);aB(a,on(new ln(),'Get groceries'));aB(a,on(new ln(),'Walk the dog'));aB(a,on(new ln(),'Start Web 2.0 company'));aB(a,on(new ln(),'Write cool app in GWT'));aB(a,on(new ln(),'Get funding'));aB(a,on(new ln(),'Take a vacation'));no(c,b);rA(c,'mail-Tasks');return c;}
function sf(){}
_=sf.prototype=new ko();_.tN=DK+'Tasks';_.tI=35;function xf(a){a.b=Fs(new sq(),"<a href='javascript:;'>Sign Out<\/a>");a.a=Fs(new sq(),"<a href='javascript:;'>About<\/a>");}
function yf(f,a){var b,c,d,e;xf(f);e=zt(new xt());b=FA(new DA());Dt(e,(jt(),mt));dB(b,(jt(),mt));c=zt(new xt());jn(c,4);At(c,f.b);At(c,f.a);d=BC((ie(),qe));At(e,d);e.Db(d,(jt(),lt));At(e,b);aB(b,Fs(new sq(),'<b>Welcome back, foo@example.com<\/b>'));aB(b,c);Du(f.b,f);Du(f.a,f);no(f,e);rA(f,'mail-TopPanel');rA(c,'mail-TopPanelLinks');return f;}
function Af(b){var a;if(b===this.b){fk('If this were implemented, you would be signed out now.');}else if(b===this.a){a=gc(new bc());gw(a);Av(a);}}
function vf(){}
_=vf.prototype=new ko();_.lb=Af;_.tN=DK+'TopPanel';_.tI=36;function EF(b,a){a;return b;}
function DF(){}
_=DF.prototype=new dF();_.tN=cL+'Throwable';_.tI=3;function FD(b,a){EF(b,a);return b;}
function ED(){}
_=ED.prototype=new DF();_.tN=cL+'Exception';_.tI=4;function iF(b,a){FD(b,a);return b;}
function hF(){}
_=hF.prototype=new ED();_.tN=cL+'RuntimeException';_.tI=5;function Cf(b,a){return b;}
function Bf(){}
_=Bf.prototype=new hF();_.tN=EK+'CommandCanceledException';_.tI=37;function sg(a){a.a=ag(new Ff(),a);a.b=iI(new gI());a.d=eg(new dg(),a);a.f=ig(new hg(),a);}
function tg(a){sg(a);return a;}
function vg(c){var a,b,d;a=kg(c.f);ng(c.f);b=null;if(yb(a,5)){b=Cf(new Bf(),xb(a,5));}else{}if(b!==null){d=w;}yg(c,false);xg(c);}
function wg(e,d){var a,b,c,f;f=false;try{yg(e,true);og(e.f,e.b.b);yj(e.a,10000);while(lg(e.f)){b=mg(e.f);c=true;try{if(b===null){return;}if(yb(b,5)){a=xb(b,5);a.A();}else{}}finally{f=pg(e.f);if(f){return;}if(c){ng(e.f);}}if(Bg(BF(),d)){return;}}}finally{if(!f){vj(e.a);yg(e,false);xg(e);}}}
function xg(a){if(!qI(a.b)&& !a.e&& !a.c){zg(a,true);yj(a.d,1);}}
function yg(b,a){b.c=a;}
function zg(b,a){b.e=a;}
function Ag(b,a){jI(b.b,a);xg(b);}
function Bg(a,b){return sE(a-b)>=100;}
function Ef(){}
_=Ef.prototype=new dF();_.tN=EK+'CommandExecutor';_.tI=38;_.c=false;_.e=false;function wj(){wj=AK;Ej=iI(new gI());{Dj();}}
function uj(a){wj();return a;}
function vj(a){if(a.b){zj(a.c);}else{Aj(a.c);}sI(Ej,a);}
function xj(a){if(!a.b){sI(Ej,a);}a.Cb();}
function yj(b,a){if(a<=0){throw cE(new bE(),'must be positive');}vj(b);b.b=false;b.c=Bj(b,a);jI(Ej,b);}
function zj(a){wj();$wnd.clearInterval(a);}
function Aj(a){wj();$wnd.clearTimeout(a);}
function Bj(b,a){wj();return $wnd.setTimeout(function(){b.B();},a);}
function Cj(){var a;a=w;{xj(this);}}
function Dj(){wj();dk(new qj());}
function pj(){}
_=pj.prototype=new dF();_.B=Cj;_.tN=EK+'Timer';_.tI=39;_.b=false;_.c=0;var Ej;function bg(){bg=AK;wj();}
function ag(b,a){bg();b.a=a;uj(b);return b;}
function cg(){if(!this.a.c){return;}vg(this.a);}
function Ff(){}
_=Ff.prototype=new pj();_.Cb=cg;_.tN=EK+'CommandExecutor$1';_.tI=40;function fg(){fg=AK;wj();}
function eg(b,a){fg();b.a=a;uj(b);return b;}
function gg(){zg(this.a,false);wg(this.a,BF());}
function dg(){}
_=dg.prototype=new pj();_.Cb=gg;_.tN=EK+'CommandExecutor$2';_.tI=41;function ig(b,a){b.d=a;return b;}
function kg(a){return nI(a.d.b,a.b);}
function lg(a){return a.c<a.a;}
function mg(b){var a;b.b=b.c;a=nI(b.d.b,b.c++);if(b.c>=b.a){b.c=0;}return a;}
function ng(a){rI(a.d.b,a.b);--a.a;if(a.b<=a.c){if(--a.c<0){a.c=0;}}a.b=(-1);}
function og(b,a){b.a=a;}
function pg(a){return a.b==(-1);}
function qg(){return lg(this);}
function rg(){return mg(this);}
function hg(){}
_=hg.prototype=new dF();_.db=qg;_.hb=rg;_.tN=EK+'CommandExecutor$CircularIterator';_.tI=42;_.a=0;_.b=(-1);_.c=0;function Eg(){Eg=AK;si=iI(new gI());{ki=new wk();Dk(ki);}}
function Fg(a){Eg();jI(si,a);}
function ah(b,a){Eg();ql(ki,b,a);}
function bh(a,b){Eg();return yk(ki,a,b);}
function ch(){Eg();return sl(ki,'button');}
function dh(){Eg();return sl(ki,'div');}
function eh(a){Eg();return sl(ki,a);}
function fh(){Eg();return sl(ki,'img');}
function gh(){Eg();return tl(ki,'checkbox');}
function hh(){Eg();return sl(ki,'label');}
function ih(){Eg();return sl(ki,'span');}
function jh(){Eg();return sl(ki,'tbody');}
function kh(){Eg();return sl(ki,'td');}
function lh(){Eg();return sl(ki,'tr');}
function mh(){Eg();return sl(ki,'table');}
function ph(b,a,d){Eg();var c;c=w;{oh(b,a,d);}}
function oh(b,a,c){Eg();var d;if(a===ri){if(Ch(b)==8192){ri=null;}}d=nh;nh=b;try{c.jb(b);}finally{nh=d;}}
function qh(b,a){Eg();ul(ki,b,a);}
function rh(a){Eg();return vl(ki,a);}
function sh(a){Eg();return wl(ki,a);}
function th(a){Eg();return xl(ki,a);}
function uh(a){Eg();return yl(ki,a);}
function vh(a){Eg();return zl(ki,a);}
function wh(a){Eg();return el(ki,a);}
function xh(a){Eg();return Al(ki,a);}
function yh(a){Eg();return Bl(ki,a);}
function zh(a){Eg();return Cl(ki,a);}
function Ah(a){Eg();return fl(ki,a);}
function Bh(a){Eg();return gl(ki,a);}
function Ch(a){Eg();return Dl(ki,a);}
function Dh(a){Eg();hl(ki,a);}
function Eh(a){Eg();return zk(ki,a);}
function Fh(a){Eg();return Ak(ki,a);}
function ci(b,a){Eg();return jl(ki,b,a);}
function ai(a){Eg();return il(ki,a);}
function bi(b,a){Eg();return Bk(ki,b,a);}
function di(a){Eg();return El(ki,a);}
function gi(a,b){Eg();return bm(ki,a,b);}
function ei(a,b){Eg();return Fl(ki,a,b);}
function fi(a,b){Eg();return am(ki,a,b);}
function hi(a){Eg();return cm(ki,a);}
function ii(a){Eg();return kl(ki,a);}
function ji(a){Eg();return ll(ki,a);}
function li(c,a,b){Eg();nl(ki,c,a,b);}
function mi(b,a){Eg();return Ek(ki,b,a);}
function ni(a){Eg();var b,c;c=true;if(si.b>0){b=xb(nI(si,si.b-1),6);if(!(c=b.nb(a))){qh(a,true);Dh(a);}}return c;}
function oi(a){Eg();if(ri!==null&&bh(a,ri)){ri=null;}Fk(ki,a);}
function pi(b,a){Eg();dm(ki,b,a);}
function qi(a){Eg();sI(si,a);}
function ti(a){Eg();em(ki,a);}
function ui(a){Eg();ri=a;ol(ki,a);}
function xi(a,b,c){Eg();hm(ki,a,b,c);}
function vi(a,b,c){Eg();fm(ki,a,b,c);}
function wi(a,b,c){Eg();gm(ki,a,b,c);}
function yi(a,b){Eg();im(ki,a,b);}
function zi(a,b){Eg();jm(ki,a,b);}
function Ai(a,b){Eg();km(ki,a,b);}
function Bi(b,a,c){Eg();lm(ki,b,a,c);}
function Ci(b,a,c){Eg();mm(ki,b,a,c);}
function Di(a,b){Eg();bl(ki,a,b);}
function Ei(){Eg();return nm(ki);}
function Fi(){Eg();return om(ki);}
var nh=null,ki=null,ri=null,si;function bj(){bj=AK;dj=tg(new Ef());}
function cj(a){bj();if(a===null){throw xE(new wE(),'cmd can not be null');}Ag(dj,a);}
var dj;function gj(b,a){if(yb(a,7)){return bh(b,xb(a,7));}return ab(Eb(b,ej),a);}
function hj(a){return gj(this,a);}
function ij(){return bb(Eb(this,ej));}
function ej(){}
_=ej.prototype=new E();_.eQ=hj;_.hC=ij;_.tN=EK+'Element';_.tI=43;function nj(a){return ab(Eb(this,jj),a);}
function oj(){return bb(Eb(this,jj));}
function jj(){}
_=jj.prototype=new E();_.eQ=nj;_.hC=oj;_.tN=EK+'Event';_.tI=44;function sj(){while((wj(),Ej).b>0){vj(xb(nI((wj(),Ej),0),8));}}
function tj(){return null;}
function qj(){}
_=qj.prototype=new dF();_.wb=sj;_.xb=tj;_.tN=EK+'Timer$1';_.tI=45;function ck(){ck=AK;gk=iI(new gI());tk=iI(new gI());{pk();}}
function dk(a){ck();jI(gk,a);}
function ek(a){ck();jI(tk,a);}
function fk(a){ck();$wnd.alert(a);}
function hk(a){ck();$doc.body.style.overflow=a?'auto':'hidden';}
function ik(){ck();var a,b;for(a=uG(gk);nG(a);){b=xb(oG(a),9);b.wb();}}
function jk(){ck();var a,b,c,d;d=null;for(a=uG(gk);nG(a);){b=xb(oG(a),9);c=b.xb();{d=c;}}return d;}
function kk(){ck();var a,b;for(a=uG(tk);nG(a);){b=xb(oG(a),10);b.yb(mk(),lk());}}
function lk(){ck();return Ei();}
function mk(){ck();return Fi();}
function nk(){ck();return $doc.documentElement.scrollLeft||$doc.body.scrollLeft;}
function ok(){ck();return $doc.documentElement.scrollTop||$doc.body.scrollTop;}
function pk(){ck();__gwt_initHandlers(function(){sk();},function(){return rk();},function(){qk();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function qk(){ck();var a;a=w;{ik();}}
function rk(){ck();var a;a=w;{return jk();}}
function sk(){ck();var a;a=w;{kk();}}
function uk(a){ck();$doc.body.style.margin=a;}
var gk,tk;function ql(c,b,a){b.appendChild(a);}
function sl(b,a){return $doc.createElement(a);}
function tl(b,c){var a=$doc.createElement('INPUT');a.type=c;return a;}
function ul(c,b,a){b.cancelBubble=a;}
function vl(b,a){return !(!a.altKey);}
function wl(b,a){return a.clientX|| -1;}
function xl(b,a){return a.clientY|| -1;}
function yl(b,a){return !(!a.ctrlKey);}
function zl(b,a){return a.currentTarget;}
function Al(b,a){return a.which||(a.keyCode|| -1);}
function Bl(b,a){return !(!a.metaKey);}
function Cl(b,a){return !(!a.shiftKey);}
function Dl(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function El(c,b){var a=$doc.getElementById(b);return a||null;}
function bm(d,a,b){var c=a[b];return c==null?null:String(c);}
function Fl(c,a,b){return !(!a[b]);}
function am(d,a,c){var b=parseInt(a[c]);if(!b){return 0;}return b;}
function cm(b,a){return a.__eventBits||0;}
function dm(c,b,a){b.removeChild(a);}
function em(g,b){var d=b.offsetLeft,h=b.offsetTop;var i=b.offsetWidth,c=b.offsetHeight;if(b.parentNode!=b.offsetParent){d-=b.parentNode.offsetLeft;h-=b.parentNode.offsetTop;}var a=b.parentNode;while(a&&a.nodeType==1){if(a.style.overflow=='auto'||(a.style.overflow=='scroll'||a.tagName=='BODY')){if(d<a.scrollLeft){a.scrollLeft=d;}if(d+i>a.scrollLeft+a.clientWidth){a.scrollLeft=d+i-a.clientWidth;}if(h<a.scrollTop){a.scrollTop=h;}if(h+c>a.scrollTop+a.clientHeight){a.scrollTop=h+c-a.clientHeight;}}var e=a.offsetLeft,f=a.offsetTop;if(a.parentNode!=a.offsetParent){e-=a.parentNode.offsetLeft;f-=a.parentNode.offsetTop;}d+=e-a.scrollLeft;h+=f-a.scrollTop;a=a.parentNode;}}
function hm(c,a,b,d){a[b]=d;}
function fm(c,a,b,d){a[b]=d;}
function gm(c,a,b,d){a[b]=d;}
function im(c,a,b){a.__listener=b;}
function jm(c,a,b){if(!b){b='';}a.innerHTML=b;}
function km(c,a,b){while(a.firstChild){a.removeChild(a.firstChild);}if(b!=null){a.appendChild($doc.createTextNode(b));}}
function lm(c,b,a,d){b.style[a]=d;}
function mm(c,b,a,d){b.style[a]=d;}
function nm(a){return $doc.body.clientHeight;}
function om(a){return $doc.body.clientWidth;}
function vk(){}
_=vk.prototype=new dF();_.tN=FK+'DOMImpl';_.tI=46;function el(b,a){return a.relatedTarget?a.relatedTarget:null;}
function fl(b,a){return a.target||null;}
function gl(b,a){return a.relatedTarget||null;}
function hl(b,a){a.preventDefault();}
function jl(f,c,d){var b=0,a=c.firstChild;while(a){var e=a.nextSibling;if(a.nodeType==1){if(d==b)return a;++b;}a=e;}return null;}
function il(d,c){var b=0,a=c.firstChild;while(a){if(a.nodeType==1)++b;a=a.nextSibling;}return b;}
function kl(c,b){var a=b.firstChild;while(a&&a.nodeType!=1)a=a.nextSibling;return a||null;}
function ll(c,a){var b=a.parentNode;if(b==null){return null;}if(b.nodeType!=1)b=null;return b||null;}
function ml(d){$wnd.__dispatchCapturedMouseEvent=function(b){if($wnd.__dispatchCapturedEvent(b)){var a=$wnd.__captureElem;if(a&&a.__listener){ph(b,a,a.__listener);b.stopPropagation();}}};$wnd.__dispatchCapturedEvent=function(a){if(!ni(a)){a.stopPropagation();a.preventDefault();return false;}return true;};$wnd.addEventListener('click',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('dblclick',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousedown',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mouseup',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousemove',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('mousewheel',$wnd.__dispatchCapturedMouseEvent,true);$wnd.addEventListener('keydown',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keyup',$wnd.__dispatchCapturedEvent,true);$wnd.addEventListener('keypress',$wnd.__dispatchCapturedEvent,true);$wnd.__dispatchEvent=function(b){var c,a=this;while(a&& !(c=a.__listener))a=a.parentNode;if(a&&a.nodeType!=1)a=null;if(c)ph(b,a,c);};$wnd.__captureElem=null;}
function nl(f,e,g,d){var c=0,b=e.firstChild,a=null;while(b){if(b.nodeType==1){if(c==d){a=b;break;}++c;}b=b.nextSibling;}e.insertBefore(g,a);}
function ol(b,a){$wnd.__captureElem=a;}
function pl(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&2?$wnd.__dispatchEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function cl(){}
_=cl.prototype=new vk();_.tN=FK+'DOMImplStandard';_.tI=47;function yk(c,a,b){if(!a&& !b){return true;}else if(!a|| !b){return false;}return a.isSameNode(b);}
function zk(c,b){try{return $doc.getBoxObjectFor(b).screenX-$doc.getBoxObjectFor($doc.documentElement).screenX;}catch(a){if(a.code==4){return 0;}throw a;}}
function Ak(c,b){try{return $doc.getBoxObjectFor(b).screenY-$doc.getBoxObjectFor($doc.documentElement).screenY;}catch(a){if(a.code==4){return 0;}throw a;}}
function Bk(d,c,e){var b=0,a=c.firstChild;while(a){if(a.isSameNode(e)){return b;}if(a.nodeType==1){++b;}a=a.nextSibling;}return -1;}
function Dk(a){ml(a);Ck(a);}
function Ck(d){$wnd.addEventListener('mouseout',function(b){var a=$wnd.__captureElem;if(a&& !b.relatedTarget){if('html'==b.target.tagName.toLowerCase()){var c=$doc.createEvent('MouseEvents');c.initMouseEvent('mouseup',true,true,$wnd,0,b.screenX,b.screenY,b.clientX,b.clientY,b.ctrlKey,b.altKey,b.shiftKey,b.metaKey,b.button,null);a.dispatchEvent(c);}}},true);$wnd.addEventListener('DOMMouseScroll',$wnd.__dispatchCapturedMouseEvent,true);}
function Ek(d,c,b){while(b){if(c.isSameNode(b)){return true;}try{b=b.parentNode;}catch(a){return false;}if(b&&b.nodeType!=1){b=null;}}return false;}
function Fk(b,a){if(a.isSameNode($wnd.__captureElem)){$wnd.__captureElem=null;}}
function bl(c,b,a){pl(c,b,a);al(c,b,a);}
function al(c,b,a){if(a&131072){b.addEventListener('DOMMouseScroll',$wnd.__dispatchEvent,false);}}
function wk(){}
_=wk.prototype=new cl();_.tN=FK+'DOMImplMozilla';_.tI=48;function qm(a){En(a);a.Eb(dh());Ci(a.D(),'position','relative');Ci(a.D(),'overflow','hidden');return a;}
function rm(a,b){Fn(a,b,a.D());}
function tm(b,c){var a;a=ho(b,c);if(a){um(c.D());}return a;}
function um(a){Ci(a,'left','');Ci(a,'top','');Ci(a,'position','');}
function vm(a){return tm(this,a);}
function pm(){}
_=pm.prototype=new Cn();_.Bb=vm;_.tN=aL+'AbsolutePanel';_.tI=49;function wm(){}
_=wm.prototype=new dF();_.tN=aL+'AbstractImagePrototype';_.tI=50;function oq(){oq=AK;bD(),fD;}
function mq(b,a){bD(),fD;pq(b,a);return b;}
function nq(b,a){if(b.c===null){b.c=yn(new xn());}jI(b.c,a);}
function pq(b,a){gC(b,a);sA(b,7041);}
function qq(a){switch(Ch(a)){case 1:if(this.c!==null){An(this.c,this);}break;case 4096:case 2048:break;case 128:case 512:case 256:break;}}
function rq(a){pq(this,a);}
function lq(){}
_=lq.prototype=new fB();_.jb=qq;_.Eb=rq;_.tN=aL+'FocusWidget';_.tI=51;_.c=null;function Bm(){Bm=AK;bD(),fD;}
function Am(b,a){bD(),fD;mq(b,a);return b;}
function Cm(a){zi(this.D(),a);}
function zm(){}
_=zm.prototype=new lq();_.Fb=Cm;_.tN=aL+'ButtonBase';_.tI=52;function an(){an=AK;bD(),fD;}
function Dm(a){bD(),fD;Am(a,ch());bn(a.D());rA(a,'gwt-Button');return a;}
function Em(b,a){bD(),fD;Dm(b);b.Fb(a);return b;}
function Fm(c,a,b){bD(),fD;Em(c,a);nq(c,b);return c;}
function bn(b){an();if(b.type=='submit'){try{b.setAttribute('type','button');}catch(a){}}}
function ym(){}
_=ym.prototype=new zm();_.tN=aL+'Button';_.tI=53;function dn(a){En(a);a.e=mh();a.d=jh();ah(a.e,a.d);a.Eb(a.e);return a;}
function fn(a,b){if(b.q!==a){return null;}return ji(b.D());}
function gn(c,b,a){xi(b,'align',a.a);}
function hn(c,b,a){Ci(b,'verticalAlign',a.a);}
function jn(b,a){wi(b.e,'cellSpacing',a);}
function kn(c,a){var b;b=fn(this,c);if(b!==null){gn(this,b,a);}}
function cn(){}
_=cn.prototype=new Cn();_.Db=kn;_.tN=aL+'CellPanel';_.tI=54;_.d=null;_.e=null;function pn(){pn=AK;bD(),fD;}
function mn(a){bD(),fD;nn(a,gh());rA(a,'gwt-CheckBox');return a;}
function on(b,a){bD(),fD;mn(b);sn(b,a);return b;}
function nn(b,a){var c;bD(),fD;Am(b,ih());b.a=a;b.b=hh();Di(b.a,hi(b.D()));Di(b.D(),0);ah(b.D(),b.a);ah(b.D(),b.b);c='check'+ ++wn;xi(b.a,'id',c);xi(b.b,'htmlFor',c);return b;}
function qn(b){var a;a=b.eb()?'checked':'defaultChecked';return ei(b.a,a);}
function rn(b,a){vi(b.a,'checked',a);vi(b.a,'defaultChecked',a);}
function sn(b,a){Ai(b.b,a);}
function tn(){yi(this.a,this);}
function un(){yi(this.a,null);rn(this,qn(this));}
function vn(a){zi(this.b,a);}
function ln(){}
_=ln.prototype=new zm();_.pb=tn;_.vb=un;_.Fb=vn;_.tN=aL+'CheckBox';_.tI=55;_.a=null;_.b=null;var wn=0;function eG(d,a,b){var c;while(a.db()){c=a.hb();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function gG(a){throw bG(new aG(),'add');}
function hG(b){var a;a=eG(this,this.fb(),b);return a!==null;}
function iG(a){var b,c,d;d=this.fc();if(a.a<d){a=mb(a,d);}b=0;for(c=this.fb();c.db();){tb(a,b++,c.hb());}if(a.a>d){tb(a,d,null);}return a;}
function dG(){}
_=dG.prototype=new dF();_.u=gG;_.w=hG;_.gc=iG;_.tN=dL+'AbstractCollection';_.tI=56;function tG(b,a){throw iE(new hE(),'Index: '+a+', Size: '+b.b);}
function uG(a){return lG(new kG(),a);}
function vG(b,a){throw bG(new aG(),'add');}
function wG(a){this.t(this.fc(),a);return true;}
function xG(e){var a,b,c,d,f;if(e===this){return true;}if(!yb(e,25)){return false;}f=xb(e,25);if(this.fc()!=f.fc()){return false;}c=uG(this);d=f.fb();while(nG(c)){a=oG(c);b=oG(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function yG(){var a,b,c,d;c=1;a=31;b=uG(this);while(nG(b)){d=oG(b);c=31*c+(d===null?0:d.hC());}return c;}
function zG(){return uG(this);}
function AG(a){throw bG(new aG(),'remove');}
function jG(){}
_=jG.prototype=new dG();_.t=vG;_.u=wG;_.eQ=xG;_.hC=yG;_.fb=zG;_.Ab=AG;_.tN=dL+'AbstractList';_.tI=57;function hI(a){{kI(a);}}
function iI(a){hI(a);return a;}
function jI(b,a){DI(b.a,b.b++,a);return true;}
function kI(a){a.a=cb();a.b=0;}
function mI(b,a){return oI(b,a)!=(-1);}
function nI(b,a){if(a<0||a>=b.b){tG(b,a);}return zI(b.a,a);}
function oI(b,a){return pI(b,a,0);}
function pI(c,b,a){if(a<0){tG(c,a);}for(;a<c.b;++a){if(yI(b,zI(c.a,a))){return a;}}return (-1);}
function qI(a){return a.b==0;}
function rI(c,a){var b;b=nI(c,a);BI(c.a,a,1);--c.b;return b;}
function sI(c,b){var a;a=oI(c,b);if(a==(-1)){return false;}rI(c,a);return true;}
function tI(d,a,b){var c;c=nI(d,a);DI(d.a,a,b);return c;}
function vI(a,b){if(a<0||a>this.b){tG(this,a);}uI(this.a,a,b);++this.b;}
function wI(a){return jI(this,a);}
function uI(a,b,c){a.splice(b,0,c);}
function xI(a){return mI(this,a);}
function yI(a,b){return a===b||a!==null&&a.eQ(b);}
function AI(a){return nI(this,a);}
function zI(a,b){return a[b];}
function CI(a){return rI(this,a);}
function BI(a,c,b){a.splice(c,b);}
function DI(a,b,c){a[b]=c;}
function EI(){return this.b;}
function FI(a){var b;if(a.a<this.b){a=mb(a,this.b);}for(b=0;b<this.b;++b){tb(a,b,zI(this.a,b));}if(a.a>this.b){tb(a,this.b,null);}return a;}
function gI(){}
_=gI.prototype=new jG();_.t=vI;_.u=wI;_.w=xI;_.bb=AI;_.Ab=CI;_.fc=EI;_.gc=FI;_.tN=dL+'ArrayList';_.tI=58;_.a=null;_.b=0;function yn(a){iI(a);return a;}
function An(d,c){var a,b;for(a=uG(d);nG(a);){b=xb(oG(a),11);b.lb(c);}}
function xn(){}
_=xn.prototype=new gI();_.tN=aL+'ClickListenerCollection';_.tI=59;function op(){op=AK;up=new ep();vp=new ep();wp=new ep();xp=new ep();yp=new ep();}
function lp(a){a.b=(jt(),lt);a.c=(st(),ut);}
function mp(a){op();dn(a);lp(a);wi(a.e,'cellSpacing',0);wi(a.e,'cellPadding',0);return a;}
function np(c,d,a){var b;if(a===up){if(d===c.a){return;}else if(c.a!==null){throw cE(new bE(),'Only one CENTER widget may be added');}}fC(d);pB(c.f,d);if(a===up){c.a=d;}b=hp(new gp(),a);hC(d,b);rp(c,d,c.b);sp(c,d,c.c);pp(c);ov(c,d);}
function pp(p){var a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,q;a=p.d;while(ai(a)>0){pi(a,ci(a,0));}l=1;d=1;for(h=uB(p.f);kB(h);){c=lB(h);e=c.p.a;if(e===wp||e===xp){++l;}else if(e===vp||e===yp){++d;}}m=rb('[Lcom.google.gwt.user.client.ui.DockPanel$TmpRow;',[125],[21],[l],null);for(g=0;g<l;++g){m[g]=new jp();m[g].b=lh();ah(a,m[g].b);}q=0;f=d-1;j=0;n=l-1;b=null;for(h=uB(p.f);kB(h);){c=lB(h);i=c.p;o=kh();i.d=o;xi(i.d,'align',i.b);Ci(i.d,'verticalAlign',i.e);xi(i.d,'width',i.f);xi(i.d,'height',i.c);if(i.a===wp){li(m[j].b,o,m[j].a);ah(o,c.D());wi(o,'colSpan',f-q+1);++j;}else if(i.a===xp){li(m[n].b,o,m[n].a);ah(o,c.D());wi(o,'colSpan',f-q+1);--n;}else if(i.a===yp){k=m[j];li(k.b,o,k.a++);ah(o,c.D());wi(o,'rowSpan',n-j+1);++q;}else if(i.a===vp){k=m[j];li(k.b,o,k.a);ah(o,c.D());wi(o,'rowSpan',n-j+1);--f;}else if(i.a===up){b=o;}}if(p.a!==null){k=m[j];li(k.b,b,k.a);ah(b,p.a.D());}}
function qp(c,d,b){var a;a=d.p;a.c=b;if(a.d!==null){Ci(a.d,'height',a.c);}}
function rp(c,d,a){var b;b=d.p;b.b=a.a;if(b.d!==null){xi(b.d,'align',b.b);}}
function sp(c,d,a){var b;b=d.p;b.e=a.a;if(b.d!==null){Ci(b.d,'verticalAlign',b.e);}}
function tp(b,c,d){var a;a=c.p;a.f=d;if(a.d!==null){Ci(a.d,'width',a.f);}}
function zp(b){var a;a=ho(this,b);if(a){if(b===this.a){this.a=null;}pp(this);}return a;}
function Ap(b,a){rp(this,b,a);}
function dp(){}
_=dp.prototype=new cn();_.Bb=zp;_.Db=Ap;_.tN=aL+'DockPanel';_.tI=60;_.a=null;var up,vp,wp,xp,yp;function ep(){}
_=ep.prototype=new dF();_.tN=aL+'DockPanel$DockLayoutConstant';_.tI=61;function hp(b,a){b.a=a;return b;}
function gp(){}
_=gp.prototype=new dF();_.tN=aL+'DockPanel$LayoutData';_.tI=62;_.a=null;_.b='left';_.c='';_.d=null;_.e='top';_.f='';function jp(){}
_=jp.prototype=new dF();_.tN=aL+'DockPanel$TmpRow';_.tI=63;_.a=0;_.b=null;function es(a){a.g=Ar(new vr());}
function fs(a){es(a);a.e=mh();a.a=jh();ah(a.e,a.a);a.Eb(a.e);sA(a,1);return a;}
function gs(b,a){if(b.f===null){b.f=ny(new my());}jI(b.f,a);}
function hs(d,c,b){var a;is(d,c);if(b<0){throw iE(new hE(),'Column '+b+' must be non-negative: '+b);}a=cq(d,c);if(a<=b){throw iE(new hE(),'Column index: '+b+', Column size: '+cq(d,c));}}
function is(c,a){var b;b=dq(c);if(a>=b||a<0){throw iE(new hE(),'Row index: '+a+', Row size: '+b);}}
function js(e,c,b,a){var d;d=cr(e.b,c,b);qs(e,d,a);return d;}
function ls(c,b,a){return b.rows[a].cells.length;}
function ms(a){return ns(a,a.a);}
function ns(b,a){return a.rows.length;}
function os(d,b){var a,c,e;c=Ah(b);for(;c!==null;c=ji(c)){if(nF(gi(c,'tagName'),'td')){e=ji(c);a=ji(e);if(bh(a,d.a)){return c;}}if(bh(c,d.a)){return null;}}return null;}
function ps(b,a){var c;if(a!=dq(b)){is(b,a);}c=lh();li(b.a,c,a);return a;}
function qs(d,c,a){var b,e;b=ii(c);e=null;if(b!==null){e=Cr(d.g,b);}if(e!==null){rs(d,e);return true;}else{if(a){zi(c,'');}return false;}}
function rs(b,c){var a;if(c.q!==b){return false;}qv(b,c);a=c.D();pi(ji(a),a);Fr(b.g,a);return true;}
function ss(a,b){xi(a.e,'border',''+b);}
function ts(b,a){b.b=a;}
function us(b,a){wi(b.e,'cellPadding',a);}
function vs(b,a){wi(b.e,'cellSpacing',a);}
function ws(b,a){b.c=a;mr(b.c);}
function xs(e,c,a,b){var d;fq(e,c,a);d=js(e,c,a,b===null);if(b!==null){zi(d,b);}}
function ys(b,a){b.d=a;}
function zs(e,b,a,d){var c;fq(e,b,a);c=js(e,b,a,d===null);if(d!==null){Ai(c,d);}}
function As(d,b,a,e){var c;fq(d,b,a);if(e!==null){fC(e);c=js(d,b,a,true);Dr(d.g,e);ah(c,e.D());ov(d,e);}}
function Bs(){return as(this.g);}
function Cs(c){var a,b,d,e,f;switch(Ch(c)){case 1:{if(this.f!==null){e=os(this,c);if(e===null){return;}f=ji(e);a=ji(f);d=bi(a,f);b=bi(f,e);py(this.f,this,d,b);}break;}default:}}
function Ds(a){return rs(this,a);}
function tq(){}
_=tq.prototype=new nv();_.fb=Bs;_.jb=Cs;_.Bb=Ds;_.tN=aL+'HTMLTable';_.tI=64;_.a=null;_.b=null;_.c=null;_.d=null;_.e=null;_.f=null;function aq(a){fs(a);ts(a,Dp(new Cp(),a));ys(a,or(new nr(),a));ws(a,kr(new jr(),a));return a;}
function cq(b,a){is(b,a);return ls(b,b.a,a);}
function dq(a){return ms(a);}
function eq(b,a){return ps(b,a);}
function fq(e,d,b){var a,c;gq(e,d);if(b<0){throw iE(new hE(),'Cannot create a column with a negative index: '+b);}a=cq(e,d);c=b+1-a;if(c>0){hq(e.a,d,c);}}
function gq(d,b){var a,c;if(b<0){throw iE(new hE(),'Cannot create a row with a negative index: '+b);}c=dq(d);for(a=c;a<=b;a++){eq(d,a);}}
function hq(f,d,c){var e=f.rows[d];for(var b=0;b<c;b++){var a=$doc.createElement('td');e.appendChild(a);}}
function Bp(){}
_=Bp.prototype=new tq();_.tN=aL+'FlexTable';_.tI=65;function Dq(b,a){b.a=a;return b;}
function Fq(c,b,a){fq(c.a,b,a);return ar(c,c.a.a,b,a);}
function ar(e,d,c,a){var b=d.rows[c].cells[a];return b==null?null:b;}
function br(c,b,a){hs(c.a,b,a);return ar(c,c.a.a,b,a);}
function cr(c,b,a){return ar(c,c.a.a,b,a);}
function dr(d,c,a,b,e){fr(d,c,a,b);gr(d,c,a,e);}
function er(e,d,a,c){var b;fq(e.a,d,a);b=ar(e,e.a.a,d,a);xi(b,'height',c);}
function fr(e,d,b,a){var c;fq(e.a,d,b);c=ar(e,e.a.a,d,b);xi(c,'align',a.a);}
function gr(d,c,b,a){fq(d.a,c,b);Ci(ar(d,d.a.a,c,b),'verticalAlign',a.a);}
function hr(c,b,a,d){fq(c.a,b,a);xi(ar(c,c.a.a,b,a),'width',d);}
function ir(c,b,a,d){var e;fq(c.a,b,a);e=d?'':'nowrap';Ci(br(c,b,a),'whiteSpace',e);}
function Cq(){}
_=Cq.prototype=new dF();_.tN=aL+'HTMLTable$CellFormatter';_.tI=66;function Dp(b,a){Dq(b,a);return b;}
function Fp(d,c,b,a){wi(Fq(d,c,b),'colSpan',a);}
function Cp(){}
_=Cp.prototype=new Cq();_.tN=aL+'FlexTable$FlexCellFormatter';_.tI=67;function jq(){jq=AK;kq=(bD(),eD);}
var kq;function Bu(a){a.Eb(dh());sA(a,131197);rA(a,'gwt-Label');return a;}
function Cu(b,a){Bu(b);av(b,a);return b;}
function Du(b,a){if(b.a===null){b.a=yn(new xn());}jI(b.a,a);}
function Eu(b,a){if(b.b===null){b.b=ev(new dv());}jI(b.b,a);}
function av(b,a){Ai(b.D(),a);}
function bv(a,b){Ci(a.D(),'whiteSpace',b?'normal':'nowrap');}
function cv(a){switch(Ch(a)){case 1:if(this.a!==null){An(this.a,this);}break;case 4:case 8:case 64:case 16:case 32:if(this.b!==null){iv(this.b,this,a);}break;case 131072:break;}}
function Au(){}
_=Au.prototype=new fB();_.jb=cv;_.tN=aL+'Label';_.tI=68;_.a=null;_.b=null;function Es(a){Bu(a);a.Eb(dh());sA(a,125);rA(a,'gwt-HTML');return a;}
function Fs(b,a){Es(b);ct(b,a);return b;}
function at(b,a,c){Fs(b,a);bv(b,c);return b;}
function ct(b,a){zi(b.D(),a);}
function sq(){}
_=sq.prototype=new Au();_.tN=aL+'HTML';_.tI=69;function vq(a){{yq(a);}}
function wq(b,a){b.b=a;vq(b);return b;}
function yq(a){while(++a.a<a.b.b.b){if(nI(a.b.b,a.a)!==null){return;}}}
function zq(a){return a.a<a.b.b.b;}
function Aq(){return zq(this);}
function Bq(){var a;if(!zq(this)){throw new wK();}a=nI(this.b.b,this.a);yq(this);return a;}
function uq(){}
_=uq.prototype=new dF();_.db=Aq;_.hb=Bq;_.tN=aL+'HTMLTable$1';_.tI=70;_.a=(-1);function kr(b,a){b.b=a;return b;}
function mr(a){if(a.a===null){a.a=eh('colgroup');li(a.b.e,a.a,0);ah(a.a,eh('col'));}}
function jr(){}
_=jr.prototype=new dF();_.tN=aL+'HTMLTable$ColumnFormatter';_.tI=71;_.a=null;function or(b,a){b.a=a;return b;}
function pr(c,a,b){zA(rr(c,a),b,true);}
function rr(b,a){gq(b.a,a);return sr(b,b.a.a,a);}
function sr(c,a,b){return a.rows[b];}
function tr(c,a,b){zA(rr(c,a),b,false);}
function ur(c,a,b){yA(rr(c,a),b);}
function nr(){}
_=nr.prototype=new dF();_.tN=aL+'HTMLTable$RowFormatter';_.tI=72;function zr(a){a.b=iI(new gI());}
function Ar(a){zr(a);return a;}
function Cr(c,a){var b;b=cs(a);if(b<0){return null;}return xb(nI(c.b,b),12);}
function Dr(b,c){var a;if(b.a===null){a=b.b.b;jI(b.b,c);}else{a=b.a.a;tI(b.b,a,c);b.a=b.a.b;}ds(c.D(),a);}
function Er(c,a,b){bs(a);tI(c.b,b,null);c.a=xr(new wr(),b,c.a);}
function Fr(c,a){var b;b=cs(a);Er(c,a,b);}
function as(a){return wq(new uq(),a);}
function bs(a){a['__widgetID']=null;}
function cs(a){var b=a['__widgetID'];return b==null?-1:b;}
function ds(a,b){a['__widgetID']=b;}
function vr(){}
_=vr.prototype=new dF();_.tN=aL+'HTMLTable$WidgetMapper';_.tI=73;_.a=null;function xr(c,a,b){c.a=a;c.b=b;return c;}
function wr(){}
_=wr.prototype=new dF();_.tN=aL+'HTMLTable$WidgetMapper$FreeNode';_.tI=74;_.a=0;_.b=null;function jt(){jt=AK;kt=ht(new gt(),'center');lt=ht(new gt(),'left');mt=ht(new gt(),'right');}
var kt,lt,mt;function ht(b,a){b.a=a;return b;}
function gt(){}
_=gt.prototype=new dF();_.tN=aL+'HasHorizontalAlignment$HorizontalAlignmentConstant';_.tI=75;_.a=null;function st(){st=AK;qt(new pt(),'bottom');tt=qt(new pt(),'middle');ut=qt(new pt(),'top');}
var tt,ut;function qt(a,b){a.a=b;return a;}
function pt(){}
_=pt.prototype=new dF();_.tN=aL+'HasVerticalAlignment$VerticalAlignmentConstant';_.tI=76;_.a=null;function yt(a){a.a=(jt(),lt);a.c=(st(),ut);}
function zt(a){dn(a);yt(a);a.b=lh();ah(a.d,a.b);xi(a.e,'cellSpacing','0');xi(a.e,'cellPadding','0');return a;}
function At(b,c){var a;a=Ct(b);ah(b.b,a);Fn(b,c,a);}
function Ct(b){var a;a=kh();gn(b,a,b.a);hn(b,a,b.c);return a;}
function Dt(b,a){b.a=a;}
function Et(c){var a,b;b=ji(c.D());a=ho(this,c);if(a){pi(this.b,b);}return a;}
function xt(){}
_=xt.prototype=new cn();_.Bb=Et;_.tN=aL+'HorizontalPanel';_.tI=77;_.b=null;function tu(){tu=AK;BJ(new cJ());}
function qu(a){tu();su(a,mu(new lu(),a));rA(a,'gwt-Image');return a;}
function ru(c,e,b,d,f,a){tu();su(c,eu(new du(),c,e,b,d,f,a));rA(c,'gwt-Image');return c;}
function su(b,a){b.a=a;}
function uu(c,e,b,d,f,a){c.a.bc(c,e,b,d,f,a);}
function vu(a){switch(Ch(a)){case 1:{break;}case 4:case 8:case 64:case 16:case 32:{break;}case 131072:break;case 32768:{break;}case 65536:{break;}}}
function Ft(){}
_=Ft.prototype=new fB();_.jb=vu;_.tN=aL+'Image';_.tI=78;_.a=null;function cu(){}
function au(){}
_=au.prototype=new dF();_.A=cu;_.tN=aL+'Image$1';_.tI=79;function ju(){}
_=ju.prototype=new dF();_.tN=aL+'Image$State';_.tI=80;function fu(){fu=AK;hu=new sC();}
function eu(d,b,f,c,e,g,a){fu();d.b=c;d.c=e;d.e=g;d.a=a;d.d=f;b.Eb(vC(hu,f,c,e,g,a));sA(b,131197);gu(d,b);return d;}
function gu(b,a){cj(new au());}
function iu(b,e,c,d,f,a){if(!oF(this.d,e)||this.b!=c||this.c!=d||this.e!=f||this.a!=a){this.d=e;this.b=c;this.c=d;this.e=f;this.a=a;tC(hu,b.D(),e,c,d,f,a);gu(this,b);}}
function du(){}
_=du.prototype=new ju();_.bc=iu;_.tN=aL+'Image$ClippedState';_.tI=81;_.a=0;_.b=0;_.c=0;_.d=null;_.e=0;var hu;function mu(b,a){a.Eb(fh());sA(a,229501);return b;}
function ou(b,e,c,d,f,a){su(b,eu(new du(),b,e,c,d,f,a));}
function lu(){}
_=lu.prototype=new ju();_.bc=ou;_.tN=aL+'Image$UnclippedState';_.tI=82;function zu(a){return (zh(a)?1:0)|(yh(a)?8:0)|(uh(a)?2:0)|(rh(a)?4:0);}
function ev(a){iI(a);return a;}
function gv(d,c,e,f){var a,b;for(a=uG(d);nG(a);){b=xb(oG(a),13);b.qb(c,e,f);}}
function hv(d,c){var a,b;for(a=uG(d);nG(a);){b=xb(oG(a),13);b.rb(c);}}
function iv(e,c,a){var b,d,f,g,h;d=c.D();g=sh(a)-Eh(d)+fi(d,'scrollLeft')+nk();h=th(a)-Fh(d)+fi(d,'scrollTop')+ok();switch(Ch(a)){case 4:gv(e,c,g,h);break;case 8:lv(e,c,g,h);break;case 64:kv(e,c,g,h);break;case 16:b=wh(a);if(!mi(d,b)){hv(e,c);}break;case 32:f=Bh(a);if(!mi(d,f)){jv(e,c);}break;}}
function jv(d,c){var a,b;for(a=uG(d);nG(a);){b=xb(oG(a),13);b.sb(c);}}
function kv(d,c,e,f){var a,b;for(a=uG(d);nG(a);){b=xb(oG(a),13);b.tb(c,e,f);}}
function lv(d,c,e,f){var a,b;for(a=uG(d);nG(a);){b=xb(oG(a),13);b.ub(c,e,f);}}
function dv(){}
_=dv.prototype=new gI();_.tN=aL+'MouseListenerCollection';_.tI=83;function xw(){xw=AK;Cw=BJ(new cJ());}
function ww(b,a){xw();qm(b);if(a===null){a=yw();}b.Eb(a);b.ib();return b;}
function zw(){xw();return Aw(null);}
function Aw(c){xw();var a,b;b=xb(bK(Cw,c),14);if(b!==null){return b;}a=null;if(Cw.c==0){Bw();}cK(Cw,c,b=ww(new rw(),a));return b;}
function yw(){xw();return $doc.body;}
function Bw(){xw();dk(new sw());}
function rw(){}
_=rw.prototype=new pm();_.tN=aL+'RootPanel';_.tI=84;var Cw;function uw(){var a,b;for(b=nH(BH((xw(),Cw)));uH(b);){a=xb(vH(b),14);if(a.eb()){a.mb();}}}
function vw(){return null;}
function sw(){}
_=sw.prototype=new dF();_.wb=uw;_.xb=vw;_.tN=aL+'RootPanel$1';_.tI=85;function Ew(a){kx(a);bx(a,false);sA(a,16384);return a;}
function Fw(b,a){Ew(b);b.dc(a);return b;}
function bx(b,a){Ci(b.D(),'overflow',a?'scroll':'auto');}
function cx(a){Ch(a)==16384;}
function Dw(){}
_=Dw.prototype=new dx();_.jb=cx;_.tN=aL+'ScrollPanel';_.tI=86;function fx(a){a.a=a.b.n!==null;}
function gx(b,a){b.b=a;fx(b);return b;}
function ix(){return this.a;}
function jx(){if(!this.a||this.b.n===null){throw new wK();}this.a=false;return this.b.n;}
function ex(){}
_=ex.prototype=new dF();_.db=ix;_.hb=jx;_.tN=aL+'SimplePanel$1';_.tI=87;function ny(a){iI(a);return a;}
function py(f,e,d,a){var b,c;for(b=uG(f);nG(b);){c=xb(oG(b),15);c.kb(e,d,a);}}
function my(){}
_=my.prototype=new gI();_.tN=aL+'TableListenerCollection';_.tI=88;function pz(a){a.a=BJ(new cJ());}
function qz(b,a){pz(b);b.d=a;b.Eb(dh());Ci(b.D(),'position','relative');b.c=cD((jq(),kq));Ci(b.c,'fontSize','0');Ci(b.c,'position','absolute');Bi(b.c,'zIndex',(-1));ah(b.D(),b.c);sA(b,1021);Di(b.c,6144);b.f=ty(new sy(),b);jz(b.f,b);rA(b,'gwt-Tree');return b;}
function rz(b,a){uy(b.f,a);}
function tz(d,a,c,b){if(b===null||bh(b,c)){return;}tz(d,a,c,ji(b));jI(a,Eb(b,ej));}
function uz(e,d,b){var a,c;a=iI(new gI());tz(e,a,e.D(),b);c=wz(e,a,0,d);if(c!==null){if(mi(cz(c),b)){iz(c,!c.f,true);return true;}else if(mi(c.D(),b)){Bz(e,c,true,!aA(e,b));return true;}}return false;}
function vz(b,a){if(!a.f){return a;}return vz(b,az(a,a.c.b-1));}
function wz(i,a,e,h){var b,c,d,f,g;if(e==a.b){return h;}c=xb(nI(a,e),7);for(d=0,f=h.c.b;d<f;++d){b=az(h,d);if(bh(b.D(),c)){g=wz(i,a,e+1,az(h,d));if(g===null){return b;}return g;}}return wz(i,a,e+1,h);}
function xz(a){var b;b=rb('[Lcom.google.gwt.user.client.ui.Widget;',[128],[12],[a.a.c],null);AH(a.a).gc(b);return cC(a,b);}
function yz(h,g){var a,b,c,d,e,f,i,j;c=bz(g);{f=g.d;a=kA(h);b=lA(h);e=Eh(f)-a;i=Fh(f)-b;j=fi(f,'offsetWidth');d=fi(f,'offsetHeight');Bi(h.c,'left',e);Bi(h.c,'top',i);Bi(h.c,'width',j);Bi(h.c,'height',d);ti(h.c);dD((jq(),kq),h.c);}}
function zz(e,d,a){var b,c;if(d===e.f){return;}c=d.g;if(c===null){c=e.f;}b=Fy(c,d);if(!a|| !d.f){if(b<c.c.b-1){Bz(e,az(c,b+1),true,true);}else{zz(e,c,false);}}else if(d.c.b>0){Bz(e,az(d,0),true,true);}}
function Az(e,c){var a,b,d;b=c.g;if(b===null){b=e.f;}a=Fy(b,c);if(a>0){d=az(b,a-1);Bz(e,vz(e,d),true,true);}else{Bz(e,b,true,true);}}
function Bz(d,b,a,c){if(b===d.f){return;}if(d.b!==null){gz(d.b,false);}d.b=b;if(c&&d.b!==null){yz(d,d.b);gz(d.b,true);}}
function Cz(b,a){wy(b.f,a);}
function Dz(b,a){if(a){dD((jq(),kq),b.c);}else{aD((jq(),kq),b.c);}}
function Ez(b,a){Fz(b,a,true);}
function Fz(c,b,a){if(b===null){if(c.b===null){return;}gz(c.b,false);c.b=null;return;}Bz(c,b,a,true);}
function aA(c,a){var b=a.nodeName;return b=='SELECT'||(b=='INPUT'||(b=='TEXTAREA'||(b=='OPTION'||(b=='BUTTON'||b=='LABEL'))));}
function bA(){var a,b;for(b=xz(this);DB(b);){a=EB(b);a.ib();}yi(this.c,this);}
function cA(){var a,b;for(b=xz(this);DB(b);){a=EB(b);a.mb();}yi(this.c,null);}
function dA(){return xz(this);}
function eA(c){var a,b,d,e,f;d=Ch(c);switch(d){case 1:{b=Ah(c);if(aA(this,b)){}else{Dz(this,true);}break;}case 4:{if(gj(vh(c),Eb(this.D(),ej))){uz(this,this.f,Ah(c));}break;}case 8:{break;}case 64:{break;}case 16:{break;}case 32:{break;}case 2048:break;case 4096:{break;}case 128:if(this.b===null){if(this.f.c.b>0){Bz(this,az(this.f,0),true,true);}return;}if(this.e==128){return;}{switch(xh(c)){case 38:{Az(this,this.b);Dh(c);break;}case 40:{zz(this,this.b,true);Dh(c);break;}case 37:{if(this.b.f){hz(this.b,false);}else{f=this.b.g;if(f!==null){Ez(this,f);}}Dh(c);break;}case 39:{if(!this.b.f){hz(this.b,true);}else if(this.b.c.b>0){Ez(this,az(this.b,0));}Dh(c);break;}}}case 512:if(d==512){if(xh(c)==9){a=iI(new gI());tz(this,a,this.D(),Ah(c));e=wz(this,a,0,this.f);if(e!==this.b){Fz(this,e,true);}}}case 256:{break;}}this.e=d;}
function fA(){lz(this.f);}
function gA(b){var a;a=xb(bK(this.a,b),16);if(a===null){return false;}kz(a,null);return true;}
function ry(){}
_=ry.prototype=new fB();_.x=bA;_.y=cA;_.fb=dA;_.jb=eA;_.pb=fA;_.Bb=gA;_.tN=aL+'Tree';_.tI=89;_.b=null;_.c=null;_.d=null;_.e=0;_.f=null;function By(a){a.c=iI(new gI());a.i=qu(new Ft());}
function Cy(d){var a,b,c,e;By(d);d.Eb(dh());d.e=mh();d.d=ih();d.b=ih();a=jh();e=lh();c=kh();b=kh();ah(d.e,a);ah(a,e);ah(e,c);ah(e,b);Ci(c,'verticalAlign','middle');Ci(b,'verticalAlign','middle');ah(d.D(),d.e);ah(d.D(),d.b);ah(c,d.i.D());ah(b,d.d);Ci(d.d,'display','inline');Ci(d.D(),'whiteSpace','nowrap');Ci(d.b,'whiteSpace','nowrap');zA(d.d,'gwt-TreeItem',true);return d;}
function Dy(b,a){Cy(b);ez(b,a);return b;}
function az(b,a){if(a<0||a>=b.c.b){return null;}return xb(nI(b.c,a),16);}
function Fy(b,a){return oI(b.c,a);}
function bz(a){var b;b=a.k;{return null;}}
function cz(a){return a.i.D();}
function dz(a){if(a.g!==null){a.g.zb(a);}else if(a.j!==null){Cz(a.j,a);}}
function ez(b,a){kz(b,null);zi(b.d,a);}
function fz(b,a){b.g=a;}
function gz(b,a){if(b.h==a){return;}b.h=a;zA(b.d,'gwt-TreeItem-selected',a);}
function hz(b,a){iz(b,a,true);}
function iz(c,b,a){if(b&&c.c.b==0){return;}c.f=b;mz(c);}
function jz(d,c){var a,b;if(d.j===c){return;}if(d.j!==null){if(d.j.b===d){Ez(d.j,null);}}d.j=c;for(a=0,b=d.c.b;a<b;++a){jz(xb(nI(d.c,a),16),c);}mz(d);}
function kz(b,a){zi(b.d,'');b.k=a;}
function mz(b){var a;if(b.j===null){return;}a=b.j.d;if(b.c.b==0){AA(b.b,false);zC((ie(),ye),b.i);return;}if(b.f){AA(b.b,true);zC((ie(),ze),b.i);}else{AA(b.b,false);zC((ie(),xe),b.i);}}
function lz(c){var a,b;mz(c);for(a=0,b=c.c.b;a<b;++a){lz(xb(nI(c.c,a),16));}}
function nz(a){if(a.g!==null||a.j!==null){dz(a);}fz(a,this);jI(this.c,a);Ci(a.D(),'marginLeft','16px');ah(this.b,a.D());jz(a,this.j);if(this.c.b==1){mz(this);}}
function oz(a){if(!mI(this.c,a)){return;}jz(a,null);pi(this.b,a.D());fz(a,null);sI(this.c,a);if(this.c.b==0){mz(this);}}
function Ay(){}
_=Ay.prototype=new hA();_.s=nz;_.zb=oz;_.tN=aL+'TreeItem';_.tI=90;_.b=null;_.d=null;_.e=null;_.f=false;_.g=null;_.h=false;_.j=null;_.k=null;function ty(b,a){b.a=a;Cy(b);return b;}
function uy(b,a){if(a.g!==null||a.j!==null){dz(a);}ah(b.a.D(),a.D());jz(a,b.j);fz(a,null);jI(b.c,a);Bi(a.D(),'marginLeft',0);}
function wy(b,a){if(!mI(b.c,a)){return;}jz(a,null);fz(a,null);sI(b.c,a);pi(b.a.D(),a.D());}
function xy(a){uy(this,a);}
function yy(a){wy(this,a);}
function sy(){}
_=sy.prototype=new Ay();_.s=xy;_.zb=yy;_.tN=aL+'Tree$1';_.tI=91;function EA(a){a.a=(jt(),lt);a.b=(st(),ut);}
function FA(a){dn(a);EA(a);xi(a.e,'cellSpacing','0');xi(a.e,'cellPadding','0');return a;}
function aB(b,d){var a,c;c=lh();a=cB(b);ah(c,a);ah(b.d,c);Fn(b,d,a);}
function cB(b){var a;a=kh();gn(b,a,b.a);hn(b,a,b.b);return a;}
function dB(b,a){b.a=a;}
function eB(c){var a,b;b=ji(c.D());a=ho(this,c);if(a){pi(this.d,ji(b));}return a;}
function DA(){}
_=DA.prototype=new cn();_.Bb=eB;_.tN=aL+'VerticalPanel';_.tI=92;function oB(b,a){b.a=rb('[Lcom.google.gwt.user.client.ui.Widget;',[128],[12],[4],null);return b;}
function pB(a,b){tB(a,b,a.b);}
function rB(b,a){if(a<0||a>=b.b){throw new hE();}return b.a[a];}
function sB(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function tB(d,e,a){var b,c;if(a<0||a>d.b){throw new hE();}if(d.b==d.a.a){c=rb('[Lcom.google.gwt.user.client.ui.Widget;',[128],[12],[d.a.a*2],null);for(b=0;b<d.a.a;++b){tb(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){tb(d.a,b,d.a[b-1]);}tb(d.a,a,e);}
function uB(a){return iB(new hB(),a);}
function vB(c,b){var a;if(b<0||b>=c.b){throw new hE();}--c.b;for(a=b;a<c.b;++a){tb(c.a,a,c.a[a+1]);}tb(c.a,c.b,null);}
function wB(b,c){var a;a=sB(b,c);if(a==(-1)){throw new wK();}vB(b,a);}
function gB(){}
_=gB.prototype=new dF();_.tN=aL+'WidgetCollection';_.tI=93;_.a=null;_.b=0;function iB(b,a){b.b=a;return b;}
function kB(a){return a.a<a.b.b-1;}
function lB(a){if(a.a>=a.b.b){throw new wK();}return a.b.a[++a.a];}
function mB(){return kB(this);}
function nB(){return lB(this);}
function hB(){}
_=hB.prototype=new dF();_.db=mB;_.hb=nB;_.tN=aL+'WidgetCollection$WidgetIterator';_.tI=94;_.a=(-1);function cC(b,a){return AB(new yB(),a,b);}
function zB(a){{CB(a);}}
function AB(a,b,c){a.b=b;zB(a);return a;}
function CB(a){++a.a;while(a.a<a.b.a){if(a.b[a.a]!==null){return;}++a.a;}}
function DB(a){return a.a<a.b.a;}
function EB(a){var b;if(!DB(a)){throw new wK();}b=a.b[a.a];CB(a);return b;}
function FB(){return DB(this);}
function aC(){return EB(this);}
function yB(){}
_=yB.prototype=new dF();_.db=FB;_.hb=aC;_.tN=aL+'WidgetIterators$1';_.tI=95;_.a=(-1);function tC(e,b,g,c,f,h,a){var d;d='url('+g+') no-repeat '+(-c+'px ')+(-f+'px');Ci(b,'background',d);Ci(b,'width',h+'px');Ci(b,'height',a+'px');}
function vC(c,f,b,e,g,a){var d;d=ih();zi(d,wC(c,f,b,e,g,a));return ii(d);}
function wC(e,g,c,f,h,b){var a,d;d='width: '+h+'px; height: '+b+'px; background: url('+g+') no-repeat '+(-c+'px ')+(-f+'px');a="<img src='"+u()+"clear.cache.gif' style='"+d+"' border='0'>";return a;}
function sC(){}
_=sC.prototype=new dF();_.tN=bL+'ClippedImageImpl';_.tI=96;function AC(){AC=AK;DC=new sC();}
function yC(c,e,b,d,f,a){AC();c.d=e;c.b=b;c.c=d;c.e=f;c.a=a;return c;}
function zC(b,a){uu(a,b.d,b.b,b.c,b.e,b.a);}
function BC(a){return ru(new Ft(),a.d,a.b,a.c,a.e,a.a);}
function CC(a){return wC(DC,a.d,a.b,a.c,a.e,a.a);}
function xC(){}
_=xC.prototype=new wm();_.tN=bL+'ClippedImagePrototype';_.tI=97;_.a=0;_.b=0;_.c=0;_.d=null;_.e=0;var DC;function bD(){bD=AK;eD=FC(new EC());fD=eD;}
function FC(a){bD();return a;}
function aD(b,a){a.blur();}
function cD(b){var a=$doc.createElement('DIV');a.tabIndex=0;return a;}
function dD(b,a){a.focus();}
function EC(){}
_=EC.prototype=new dF();_.tN=bL+'FocusImpl';_.tI=98;var eD,fD;function gD(){}
_=gD.prototype=new dF();_.tN=bL+'PopupImpl';_.tI=99;function nD(){nD=AK;qD=rD();}
function mD(a){nD();return a;}
function oD(b){var a;a=dh();if(qD){zi(a,'<div><\/div>');cj(jD(new iD(),b,a));}return a;}
function pD(b,a){return qD?ii(a):a;}
function rD(){nD();if(navigator.userAgent.indexOf('Macintosh')!= -1){return true;}return false;}
function hD(){}
_=hD.prototype=new gD();_.tN=bL+'PopupImplMozilla';_.tI=100;var qD;function jD(b,a,c){b.a=c;return b;}
function lD(){Ci(this.a,'overflow','auto');}
function iD(){}
_=iD.prototype=new dF();_.A=lD;_.tN=bL+'PopupImplMozilla$1';_.tI=101;function uD(){}
_=uD.prototype=new hF();_.tN=cL+'ArrayStoreException';_.tI=102;function zD(a,b){if(b<2||b>36){return (-1);}if(a>=48&&a<48+tE(b,10)){return a-48;}if(a>=97&&a<b+97-10){return a-97+10;}if(a>=65&&a<b+65-10){return a-65+10;}return (-1);}
function AD(){}
_=AD.prototype=new hF();_.tN=cL+'ClassCastException';_.tI=103;function cE(b,a){iF(b,a);return b;}
function bE(){}
_=bE.prototype=new hF();_.tN=cL+'IllegalArgumentException';_.tI=104;function fE(b,a){iF(b,a);return b;}
function eE(){}
_=eE.prototype=new hF();_.tN=cL+'IllegalStateException';_.tI=105;function iE(b,a){iF(b,a);return b;}
function hE(){}
_=hE.prototype=new hF();_.tN=cL+'IndexOutOfBoundsException';_.tI=106;function DE(){DE=AK;{cF();}}
function EE(a){DE();return isNaN(a);}
function FE(e,d,c,h){DE();var a,b,f,g;if(e===null){throw BE(new AE(),'Unable to parse null');}b=rF(e);f=b>0&&lF(e,0)==45?1:0;for(a=f;a<b;a++){if(zD(lF(e,a),d)==(-1)){throw BE(new AE(),'Could not parse '+e+' in radix '+d);}}g=aF(e,d);if(EE(g)){throw BE(new AE(),'Unable to parse '+e);}else if(g<c||g>h){throw BE(new AE(),'The string '+e+' exceeds the range for the requested data type');}return g;}
function aF(b,a){DE();return parseInt(b,a);}
function cF(){DE();bF=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i;}
var bF=null;function lE(){lE=AK;DE();}
function oE(a){lE();return pE(a,10);}
function pE(b,a){lE();return Ab(FE(b,a,(-2147483648),2147483647));}
var mE=2147483647,nE=(-2147483648);function sE(a){return a<0?-a:a;}
function tE(a,b){return a<b?a:b;}
function uE(){}
_=uE.prototype=new hF();_.tN=cL+'NegativeArraySizeException';_.tI=107;function xE(b,a){iF(b,a);return b;}
function wE(){}
_=wE.prototype=new hF();_.tN=cL+'NullPointerException';_.tI=108;function BE(b,a){cE(b,a);return b;}
function AE(){}
_=AE.prototype=new bE();_.tN=cL+'NumberFormatException';_.tI=109;function lF(b,a){return b.charCodeAt(a);}
function oF(b,a){if(!yb(a,1))return false;return vF(b,a);}
function nF(b,a){if(a==null)return false;return b==a||b.toLowerCase()==a.toLowerCase();}
function pF(b,a){return b.indexOf(a);}
function qF(c,b,a){return c.indexOf(b,a);}
function rF(a){return a.length;}
function sF(b,a){return b.substr(a,b.length-a);}
function tF(c,a,b){return c.substr(a,b-a);}
function uF(c){var a=c.replace(/^(\s*)/,'');var b=a.replace(/\s*$/,'');return b;}
function vF(a,b){return String(a)==b;}
function wF(a){return oF(this,a);}
function yF(){var a=xF;if(!a){a=xF={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
_=String.prototype;_.eQ=wF;_.hC=yF;_.tN=cL+'String';_.tI=2;var xF=null;function BF(){return new Date().getTime();}
function CF(a){return A(a);}
function bG(b,a){iF(b,a);return b;}
function aG(){}
_=aG.prototype=new hF();_.tN=cL+'UnsupportedOperationException';_.tI=111;function lG(b,a){b.c=a;return b;}
function nG(a){return a.a<a.c.fc();}
function oG(a){if(!nG(a)){throw new wK();}return a.c.bb(a.b=a.a++);}
function pG(a){if(a.b<0){throw new eE();}a.c.Ab(a.b);a.a=a.b;a.b=(-1);}
function qG(){return nG(this);}
function rG(){return oG(this);}
function kG(){}
_=kG.prototype=new dF();_.db=qG;_.hb=rG;_.tN=dL+'AbstractList$IteratorImpl';_.tI=112;_.a=0;_.b=(-1);function zH(f,d,e){var a,b,c;for(b=wJ(f.z());pJ(b);){a=qJ(b);c=a.E();if(d===null?c===null:d.eQ(c)){if(e){rJ(b);}return a;}}return null;}
function AH(b){var a;a=b.z();return DG(new CG(),b,a);}
function BH(b){var a;a=aK(b);return lH(new kH(),b,a);}
function CH(a){return zH(this,a,false)!==null;}
function DH(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!yb(d,26)){return false;}f=xb(d,26);c=AH(this);e=f.gb();if(!dI(c,e)){return false;}for(a=FG(c);gH(a);){b=hH(a);h=this.cb(b);g=f.cb(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function EH(b){var a;a=zH(this,b,false);return a===null?null:a.ab();}
function FH(){var a,b,c;b=0;for(c=wJ(this.z());pJ(c);){a=qJ(c);b+=a.hC();}return b;}
function aI(){return AH(this);}
function BG(){}
_=BG.prototype=new dF();_.v=CH;_.eQ=DH;_.cb=EH;_.hC=FH;_.gb=aI;_.tN=dL+'AbstractMap';_.tI=113;function dI(e,b){var a,c,d;if(b===e){return true;}if(!yb(b,27)){return false;}c=xb(b,27);if(c.fc()!=e.fc()){return false;}for(a=c.fb();a.db();){d=a.hb();if(!e.w(d)){return false;}}return true;}
function eI(a){return dI(this,a);}
function fI(){var a,b,c;a=0;for(b=this.fb();b.db();){c=b.hb();if(c!==null){a+=c.hC();}}return a;}
function bI(){}
_=bI.prototype=new dG();_.eQ=eI;_.hC=fI;_.tN=dL+'AbstractSet';_.tI=114;function DG(b,a,c){b.a=a;b.b=c;return b;}
function FG(b){var a;a=wJ(b.b);return eH(new dH(),b,a);}
function aH(a){return this.a.v(a);}
function bH(){return FG(this);}
function cH(){return this.b.a.c;}
function CG(){}
_=CG.prototype=new bI();_.w=aH;_.fb=bH;_.fc=cH;_.tN=dL+'AbstractMap$1';_.tI=115;function eH(b,a,c){b.a=c;return b;}
function gH(a){return a.a.db();}
function hH(b){var a;a=b.a.hb();return a.E();}
function iH(){return gH(this);}
function jH(){return hH(this);}
function dH(){}
_=dH.prototype=new dF();_.db=iH;_.hb=jH;_.tN=dL+'AbstractMap$2';_.tI=116;function lH(b,a,c){b.a=a;b.b=c;return b;}
function nH(b){var a;a=wJ(b.b);return sH(new rH(),b,a);}
function oH(a){return FJ(this.a,a);}
function pH(){return nH(this);}
function qH(){return this.b.a.c;}
function kH(){}
_=kH.prototype=new dG();_.w=oH;_.fb=pH;_.fc=qH;_.tN=dL+'AbstractMap$3';_.tI=117;function sH(b,a,c){b.a=c;return b;}
function uH(a){return a.a.db();}
function vH(a){var b;b=a.a.hb().ab();return b;}
function wH(){return uH(this);}
function xH(){return vH(this);}
function rH(){}
_=rH.prototype=new dF();_.db=wH;_.hb=xH;_.tN=dL+'AbstractMap$4';_.tI=118;function DJ(){DJ=AK;eK=kK();}
function AJ(a){{CJ(a);}}
function BJ(a){DJ();AJ(a);return a;}
function CJ(a){a.a=cb();a.d=db();a.b=Eb(eK,E);a.c=0;}
function EJ(b,a){if(yb(a,1)){return oK(b.d,xb(a,1))!==eK;}else if(a===null){return b.b!==eK;}else{return nK(b.a,a,a.hC())!==eK;}}
function FJ(a,b){if(a.b!==eK&&mK(a.b,b)){return true;}else if(jK(a.d,b)){return true;}else if(hK(a.a,b)){return true;}return false;}
function aK(a){return uJ(new lJ(),a);}
function bK(c,a){var b;if(yb(a,1)){b=oK(c.d,xb(a,1));}else if(a===null){b=c.b;}else{b=nK(c.a,a,a.hC());}return b===eK?null:b;}
function cK(c,a,d){var b;{b=c.b;c.b=d;}if(b===eK){++c.c;return null;}else{return b;}}
function dK(c,a){var b;if(yb(a,1)){b=rK(c.d,xb(a,1));}else if(a===null){b=c.b;c.b=Eb(eK,E);}else{b=qK(c.a,a,a.hC());}if(b===eK){return null;}else{--c.c;return b;}}
function fK(e,c){DJ();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.u(a[f]);}}}}
function gK(d,a){DJ();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=gJ(c.substring(1),e);a.u(b);}}}
function hK(f,h){DJ();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.ab();if(mK(h,d)){return true;}}}}return false;}
function iK(a){return EJ(this,a);}
function jK(c,d){DJ();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(mK(d,a)){return true;}}}return false;}
function kK(){DJ();}
function lK(){return aK(this);}
function mK(a,b){DJ();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function pK(a){return bK(this,a);}
function nK(f,h,e){DJ();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.E();if(mK(h,d)){return c.ab();}}}}
function oK(b,a){DJ();return b[':'+a];}
function qK(f,h,e){DJ();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.E();if(mK(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.ab();}}}}
function rK(c,a){DJ();a=':'+a;var b=c[a];delete c[a];return b;}
function cJ(){}
_=cJ.prototype=new BG();_.v=iK;_.z=lK;_.cb=pK;_.tN=dL+'HashMap';_.tI=119;_.a=null;_.b=null;_.c=0;_.d=null;var eK;function eJ(b,a,c){b.a=a;b.b=c;return b;}
function gJ(a,b){return eJ(new dJ(),a,b);}
function hJ(b){var a;if(yb(b,28)){a=xb(b,28);if(mK(this.a,a.E())&&mK(this.b,a.ab())){return true;}}return false;}
function iJ(){return this.a;}
function jJ(){return this.b;}
function kJ(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function dJ(){}
_=dJ.prototype=new dF();_.eQ=hJ;_.E=iJ;_.ab=jJ;_.hC=kJ;_.tN=dL+'HashMap$EntryImpl';_.tI=120;_.a=null;_.b=null;function uJ(b,a){b.a=a;return b;}
function wJ(a){return nJ(new mJ(),a.a);}
function xJ(c){var a,b,d;if(yb(c,28)){a=xb(c,28);b=a.E();if(EJ(this.a,b)){d=bK(this.a,b);return mK(a.ab(),d);}}return false;}
function yJ(){return wJ(this);}
function zJ(){return this.a.c;}
function lJ(){}
_=lJ.prototype=new bI();_.w=xJ;_.fb=yJ;_.fc=zJ;_.tN=dL+'HashMap$EntrySet';_.tI=121;function nJ(c,b){var a;c.c=b;a=iI(new gI());if(c.c.b!==(DJ(),eK)){jI(a,eJ(new dJ(),null,c.c.b));}gK(c.c.d,a);fK(c.c.a,a);c.a=uG(a);return c;}
function pJ(a){return nG(a.a);}
function qJ(a){return a.b=xb(oG(a.a),28);}
function rJ(a){if(a.b===null){throw fE(new eE(),'Must call next() before remove().');}else{pG(a.a);dK(a.c,a.b.E());a.b=null;}}
function sJ(){return pJ(this);}
function tJ(){return qJ(this);}
function mJ(){}
_=mJ.prototype=new dF();_.db=sJ;_.hb=tJ;_.tN=dL+'HashMap$EntrySetIterator';_.tI=122;_.a=null;_.b=null;function wK(){}
_=wK.prototype=new hF();_.tN=dL+'NoSuchElementException';_.tI=123;function tD(){ee(be(new zc()));}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{tD();}catch(a){b(d);}else{tD();}}
var Db=[{},{18:1},{1:1,18:1,23:1,24:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{2:1,18:1},{18:1},{18:1},{18:1},{18:1,19:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{6:1,12:1,17:1,18:1,19:1,20:1},{6:1,12:1,13:1,17:1,18:1,19:1,20:1},{6:1,12:1,13:1,17:1,18:1,19:1,20:1},{11:1,18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{11:1,18:1},{18:1,22:1},{6:1,12:1,17:1,18:1,19:1,20:1},{10:1,18:1},{5:1,18:1},{12:1,18:1,19:1,20:1},{4:1,18:1},{11:1,12:1,15:1,18:1,19:1,20:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{11:1,12:1,18:1,19:1,20:1},{3:1,18:1},{18:1},{8:1,18:1},{8:1,18:1},{8:1,18:1},{18:1},{2:1,7:1,18:1},{2:1,18:1},{9:1,18:1},{18:1},{18:1},{18:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{18:1},{18:1,25:1},{18:1,25:1},{18:1,25:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{18:1,21:1},{12:1,17:1,18:1,19:1,20:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{12:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{12:1,17:1,18:1,19:1,20:1},{12:1,18:1,19:1,20:1},{5:1,18:1},{18:1},{18:1},{18:1},{18:1,25:1},{12:1,14:1,17:1,18:1,19:1,20:1},{9:1,18:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1,25:1},{12:1,17:1,18:1,19:1,20:1},{16:1,18:1,19:1},{16:1,18:1,19:1},{12:1,17:1,18:1,19:1,20:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{5:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{3:1,18:1},{18:1,24:1},{3:1,18:1},{18:1},{18:1,26:1},{18:1,27:1},{18:1,27:1},{18:1},{18:1},{18:1},{18:1,26:1},{18:1,28:1},{18:1,27:1},{18:1},{3:1,18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1},{18:1}];if (com_google_gwt_sample_mail_Mail) {  var __gwt_initHandlers = com_google_gwt_sample_mail_Mail.__gwt_initHandlers;  com_google_gwt_sample_mail_Mail.onScriptLoad(gwtOnLoad);}})();