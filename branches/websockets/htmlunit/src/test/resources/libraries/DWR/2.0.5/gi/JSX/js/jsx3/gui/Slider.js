/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Slider",jsx3.gui.Block,[jsx3.gui.Form],function(l,r){var zc=jsx3.gui.Event;l.HORIZONTAL=0;l.VERTICAL=1;l.VC=100;l.Tx=15;l.ro=7;l.Av=l.Tx;l.iz=15;l.iy=100;l.nu="jsx:///images/slider/top.gif";l.CA="jsx:///images/slider/bottom.gif";l.OF="jsx:///images/slider/left.gif";l.oy="jsx:///images/slider/right.gif";l.TA="filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#aaffffff, EndColorStr=#FF9090af);";l.YD="filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr=#aaffffff, EndColorStr=#FF9090af);";l.UF="border:solid 1px #9898a5;border-right-color:#ffffff;border-bottom-color:#ffffff;";l.IB="border:solid 1px #9898a5;border-right-color:#ffffff;border-top-color:#ffffff;";r.jsxlength=100;r.jsxvalue=0;r.jsxpainttrack=jsx3.Boolean.TRUE;r.jsxtrackclick=jsx3.Boolean.TRUE;r.jsximg=null;r.init=function(n,m,h,a){this.jsxsuper(n,m,h);this.jsxlength=a;};r.getValue=function(){return this.jsxvalue!=null?this.jsxvalue:0;};r.setValue=function(n){this.jsxvalue=n==null?null:Math.max(0,Math.min(l.iy,Number(n)));this.rt();this.C5();return this;};r.doValidate=function(){return this.setValidationState(jsx3.gui.Form.STATEVALID).getValidationState();};r.getLength=function(){return this.jsxlength!=null?this.jsxlength:l.VC;};r.setLength=function(g,s){this.jsxlength=g;this.C5();if(s)this.repaint();return this;};r.setWidth=function(g,i){if(this.getOrientation()==l.HORIZONTAL)this.setLength(g,i);return this;};r.setHeight=function(m,s){if(this.getOrientation()==l.VERTICAL)this.setLength(m,s);return this;};r.getOrientation=function(){return this.jsxorientation!=null?this.jsxorientation:l.HORIZONTAL;};r.setOrientation=function(m){this.jsxorientation=m;this.C5();return this;};r.getPaintTrack=function(){return this.jsxpainttrack!=null?this.jsxpainttrack:jsx3.Boolean.TRUE;};r.setPaintTrack=function(h){this.jsxpainttrack=h;return this;};r.getTrackClickable=function(){return this.jsxtrackclick!=null?this.jsxtrackclick:jsx3.Boolean.TRUE;};r.setTrackClickable=function(o){this.jsxtrackclick=o;return this;};r.getHandleImage=function(){return this.jsximg;};r.setHandleImage=function(k){this.jsximg=k;return this;};r.rt=function(){var E=this.getRendered();if(E!=null){var qc=jsx3.html.selectSingleElm(E,0,0);var Qb=jsx3.html.selectSingleElm(E,0,1);if(this.getOrientation()==l.HORIZONTAL){var pc=parseInt(qc.offsetWidth)-parseInt(Qb.offsetWidth);Qb.style.left=Math.round(this.getValue()*pc/l.iy)+"px";Qb.style.top=Math.round((l.Tx-l.Av)/2)+"px";}else{var Lb=parseInt(qc.offsetHeight)-parseInt(Qb.offsetHeight);Qb.style.left=Math.round((l.Tx-l.Av)/2)+"px";Qb.style.top=Lb-Math.round(this.getValue()*Lb/l.iy)+"px";}}};r.k7=function(d,a,n){var jc=this.RL(true,d);if(a){var jb=d.width!=null&&jc.implicit.width!=d.width||d.parentwidth!=jc.implicit.parentwidth&&typeof(jc.implicit.width)=="string"&&jc.implicit.width.indexOf("%")>0||d.height!=null&&jc.implicit.height!=d.height||d.parentheight!=jc.implicit.parentheight&&typeof(jc.implicit.height)=="string"&&jc.implicit.height.indexOf("%")>0;jc.recalculate(d,a,n);var Ib=jc.pQ(0);Ib.recalculate({width:jc.XK(),height:jc.P5()},a?a.childNodes[0]:null,n);var xb=Ib.pQ(0);xb.recalculate({width:Ib.XK(),height:Ib.P5()},a?a.childNodes[0].childNodes[0]:null,n);if(jb)this.rt();}};r.T5=function(f){if(this.getParent()&&(f==null||isNaN(f.parentwidth)||isNaN(f.parentheight))){f=this.getParent().IO(this);}else{if(f==null){f={};}}var mb=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==jsx3.gui.Block.RELATIVE);var wb=mb?null:this.getLeft();var xb=mb?null:this.getTop();var Wb=this.getOrientation()==l.HORIZONTAL;f.boxtype=mb?"relativebox":"box";f.tagname="span";if(f.left==null&&wb!=null)f.left=wb;if(f.top==null&&xb!=null)f.top=xb;if(Wb){f.height=l.Tx;f.width=this.getLength();}else{f.height=this.getLength();f.width=l.Tx;}var z=this.getMargin();if(mb&&z!=null&&z!="")f.margin=z;var oc=new jsx3.gui.Painted.Box(f);var nb={};nb.tagname="div";nb.boxtype="inline";nb.width=oc.XK();nb.height=oc.P5();var Bc=Math.round((l.Tx-l.ro)/2)+" ";var qb="0 ";nb.padding=Wb?Bc+qb+Bc+qb:qb+Bc+qb+Bc;var ec=new jsx3.gui.Painted.Box(nb);oc.W8(ec);nb={};nb.tagname="div";nb.boxtype="inline";if(Wb){nb.height=l.ro;nb.width=ec.XK();}else{nb.height=ec.P5();nb.width=l.ro;}nb.border=this.getBorder()?this.getBorder():this.getOrientation()==l.HORIZONTAL?l.UF:l.IB;var H=new jsx3.gui.Painted.Box(nb);ec.W8(H);var Bb=Wb?Math.round(this.getValue()/l.iy*(ec.XK()-l.iz)):Math.floor((l.Tx-l.Av)/2);var Jc=Wb?Math.floor((l.Tx-l.Av)/2):Math.round(ec.P5()-l.iz-this.getValue()/l.iy*(ec.P5()-l.iz));var Fc=Wb?l.iz:l.Av;var W=Wb?l.Av:l.iz;nb={};nb.tagname="div";nb.boxtype="box";nb.left=Bb;nb.top=Jc;nb.width=Fc;nb.height=W;var hc=new jsx3.gui.Painted.Box(nb);oc.W8(hc);nb={};nb.tagname="div";nb.boxtype="inline";nb.width=Fc;nb.height=W;var w=new jsx3.gui.Painted.Box(nb);hc.W8(w);return oc;};r.DY=function(a,d){if(!a.hasModifier()){if(a.isArrowKey()){if(a.upArrow()||a.rightArrow())this.Vd(a,1);else{if(a.downArrow()||a.leftArrow())this.Vd(a,-1);}a.cancelAll();}}};r.U2=function(a,d){var D=a.getWheelDelta();if(D!=0){this.Vd(a,D>0?1:-1);}a.cancelBubble();};r.Vd=function(d,f){var Jc;if(f>0){if(this.jsxvalue>=l.iy)return;Jc=Math.floor(this.jsxvalue+f);while(Jc<l.iy){if(this.jsxvalue<this.constrainValue(Jc))break;Jc=Jc+f;}}else{if(this.jsxvalue<=0)return;Jc=Math.ceil(this.jsxvalue+f);while(Jc>0){if(this.jsxvalue>this.constrainValue(Jc))break;Jc=Jc+f;}}Jc=this.constrainValue(Jc);if(Jc!=this.jsxvalue){var Mb=this.doEvent(jsx3.gui.Interactive.CHANGE,{objEVENT:d.event,fpPREVIOUS:this.jsxvalue,fpVALUE:Jc});if(Mb!==false)this.setValue(Jc);}};l.s5={};l.s5[zc.KEYDOWN]=true;l.s5[zc.MOUSEWHEEL]=true;r.paint=function(){this.applyDynamicProperties();var Kb=this.getOrientation()==l.HORIZONTAL;var T=this.getEnabled()==jsx3.gui.Form.STATEENABLED;var Mb=this.lM(l.s5,0);var yc=this.renderAttributes(null,true);var Rb=this.RL(true);Rb.setAttributes("id=\""+this.getId()+"\" class=\"jsx30slider\" "+"label=\""+this.getName()+"\" "+this.CI()+this.vH()+Mb+yc);Rb.setStyles(this.A0()+this.d9()+this.T1()+this.MU());var _b=Rb.pQ(0);var cc=!this.getPaintTrack()?"visibility:hidden;":"";var nb=this.getTrackClickable()&&T?this.RX(zc.CLICK,"xt",2):"";var E=_b.pQ(0);E.setStyles(this.UZ()+this.K2()+cc);E.setAttributes(" class=\"jsx30slider_track\" "+nb);var Sb=this.getHandleImage();if(Sb==null)Sb=Kb?l.nu:l.OF;var fb=Rb.pQ(1);fb.setAttributes("class=\"handle\""+(T?this.RX(zc.MOUSEDOWN,"kB",2):""));var Eb=fb.pQ(0);Eb.setAttributes(" class=\"handle"+(T?"":"_disabled")+"\" unselectable=\"on\"");Eb.setStyles("background-image:url("+this.getUriResolver().resolveURI(Sb)+");");return Rb.paint().join(_b.paint().join(E.paint().join("&#160;")+fb.paint().join(Eb.paint().join(""))));};r.kB=function(p,h){if(!p.leftButton())return;p.cancelBubble();p.cancelReturn();var G=this.getOrientation()==l.HORIZONTAL;var cc=this;jsx3.gui.Interactive.DM(p,h,function(s,q){return cc.Pw(p,s,q);});zc.subscribe(zc.MOUSEMOVE,this,"Th");zc.subscribe(zc.MOUSEUP,this,"Vl");};r.xt=function(m,s){if(!m.leftButton())return;var nb=this.getOrientation()==l.HORIZONTAL;var Ub=this.hj(nb?m.getOffsetX():m.getOffsetY());Ub=this.constrainValue(Ub);if(this.jsxvalue!=Ub){var ib=this.doEvent(jsx3.gui.Interactive.CHANGE,{objEVENT:m,fpPREVIOUS:this.jsxvalue,fpVALUE:Ub});if(ib!==false)this.setValue(Ub);}};r.Pw=function(s,g,f){var Bb=this.getRendered(s);var Gb=this.getOrientation()==l.HORIZONTAL;var Ab=0,A=0;if(Bb!=null){var D=jsx3.html.selectSingleElm(Bb,0,0);var dc=jsx3.html.selectSingleElm(Bb,0,1);if(Gb){A=dc.offsetY;var Vb=l.iy*g/(D.offsetWidth-dc.offsetWidth);Vb=this.constrainValue(Vb);Ab=Math.round(Vb*(D.offsetWidth-dc.offsetWidth)/l.iy);}else{Ab=dc.offsetX;var F=l.iy*f/(D.offsetHeight-dc.offsetHeight);F=this.constrainValue(F);A=Math.round(F*(D.offsetHeight-dc.offsetHeight)/l.iy);}}return [Ab,A];};r.Th=function(p){var Ic=this.constrainValue(this.hj());this.doEvent(jsx3.gui.Interactive.INCR_CHANGE,{objEVENT:p.event,fpVALUE:Ic});};r.Vl=function(o){jsx3.EventHelp.reset();zc.unsubscribe(zc.MOUSEMOVE,this,"Th");zc.unsubscribe(zc.MOUSEUP,this,"Vl");var Nc=this.constrainValue(this.hj());var ob=this.doEvent(jsx3.gui.Interactive.CHANGE,{objEVENT:o.event,fpPREVIOUS:this.jsxvalue,fpVALUE:Nc});if(ob===false){this.setValue(this.jsxvalue);}else{this.jsxvalue=Nc;this.C5();}};r.hj=function(e){var Ub=this.getRendered();if(Ub!=null){var tc=this.getOrientation()==l.HORIZONTAL;var Nb=jsx3.html.selectSingleElm(Ub,0,0);var oc=jsx3.html.selectSingleElm(Ub,0,1);return tc?l.iy*(e!=null?e:oc.offsetLeft)/(Nb.offsetWidth-oc.offsetWidth):l.iy*(1-(e!=null?e:oc.offsetTop)/(Nb.offsetHeight-oc.offsetHeight));}else{return 0;}};r.constrainValue=function(h){return Math.max(0,Math.min(l.iy,h));};r.K2=function(){var oc=this.getBackground()||"";return oc||this.UZ()?oc+";":this.getOrientation()==l.HORIZONTAL?l.TA:l.YD;};l.getVersion=function(){return "3.0.00";};});jsx3.Slider=jsx3.gui.Slider;
