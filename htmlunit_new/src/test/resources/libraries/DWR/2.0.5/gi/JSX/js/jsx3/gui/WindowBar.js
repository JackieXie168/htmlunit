/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.WindowBar",jsx3.gui.Block,null,function(a,f){a.DEFAULTHEIGHT=26;a.DEFAULTBACKGROUND=jsx3.html.getCSSFade(false);a.DEFAULTBG="#c8c8d5";a.DEFAULTBGCAPTION="#aaaacb";a.DEFAULTBORDER="solid 1px #e8e8f5;solid 0px;solid 1px #a8a8b5;solid 0px;";a.DEFAULTBORDERCAPTION="solid 1px #9898a5";a.DEFAULTFONTWEIGHT=jsx3.gui.Block.FONTBOLD;a.DEFAULTFONTSIZE=11;a.TYPECAPTION=0;a.TYPETOOL=1;a.TYPEMENU=2;a.TYPETASK=3;f.init=function(s,b){this.jsxsuper(s);this.setRelativePosition(jsx3.gui.Block.RELATIVE);if(b!=null)this.setType(b);};f.getMaskProperties=function(){return jsx3.gui.Block.MASK_NO_EDIT;};f.getType=function(){return this.jsxbartype==null?a.TYPECAPTION:this.jsxbartype;};f.setType=function(b){this.jsxbartype=b;return this;};f.setText=function(i,d){this.jsxsuper(i,d);if(d){if(this.getType()==a.TYPECAPTION){var Bc=this.getParent();if(Bc instanceof jsx3.gui.Dialog&&Bc.getRendered()!=null){Bc.nt();}}}return this;};f.doBeginMove=function(m,s){if(jsx3.html.getJSXParent(m.srcElement())==this)this.getParent().doBeginMove(m,s);};a.s5={};a.s5[jsx3.gui.Event.MOUSEDOWN]="doBeginMove";f.k7=function(g,d,k){this.B_(g,d,k,2);};f.T5=function(r){this.applyDynamicProperties();if(this.getParent()&&(r==null||!isNaN(r.parentwidth)&&!isNaN(r.parentheight))){r=this.getParent().IO(this);}else{if(r==null){r={};}}var Gb=this.getBorder();var D=this.getRelativePosition()!==0;if(r.left==null&&!D&&!jsx3.util.strEmpty(this.getLeft()))r.left=this.getLeft();if(r.top==null&&!D&&!jsx3.util.strEmpty(this.getTop()))r.top=this.getTop();if(r.width==null)r.width="100%";if(r.height==null)r.height=a.DEFAULTHEIGHT;r.boxtype=D?"inline":"box";r.tagname="div";r.padding="0 0 0 4";r.border=Gb!=null?Gb:this.getType()==a.TYPECAPTION?a.DEFAULTBORDERCAPTION:a.DEFAULTBORDER;var oc=new jsx3.gui.Painted.Box(r);if(this.getType()==a.TYPECAPTION){var Jb={};Jb.left=6;Jb.top=6;Jb.tagname="span";Jb.boxtype="box";var bc=new jsx3.gui.Painted.Box(Jb);oc.W8(bc);}return oc;};f.paint=function(){this.applyDynamicProperties();var ec=this.RL(true);var t=null,qc=null,kb=null;if(this.getType()==a.TYPECAPTION){var Sb=ec.pQ(0);Sb.setAttributes("class=\"jsx30windowbar_lbl\"");Sb.setStyles(this.QP()+this.D6()+this.oY()+this.g0());kb=Sb.paint().join(this.AN());t="cursor:move;";qc=this.lM(a.s5,0);}else{t="cursor:default;";qc="";kb="";}var Ub=this.renderAttributes(null,true);ec.setAttributes("id=\""+this.getId()+"\" label=\""+this.getName()+"\" "+this.vH()+this.CI()+qc+" class=\"jsx30windowbar\""+Ub);ec.setStyles(t+this.eQ()+this.UZ()+this.d9()+this.K2()+this.iN());return ec.paint().join(kb+this.paintChildren());};f.CI=function(){return this.jsxsuper(this.getIndex()||Number(0));};f.UZ=function(){var eb=this.getBackgroundColor();return eb==null||eb!=""?"background-color:"+(eb?eb:this.getType()==a.TYPECAPTION?a.DEFAULTBGCAPTION:a.DEFAULTBG)+";":"";};f.K2=function(){if(this.getType()==a.TYPEMENU)return "";var lc=this.getBackground();return lc==null?a.DEFAULTBACKGROUND:lc;};f.getHeight=function(){var ib=this.jsxsuper();if(ib==null||isNaN(ib))ib=a.DEFAULTHEIGHT;return ib;};f.D6=function(){return this.getFontWeight()?"font-weight:"+this.getFontWeight()+";":"font-weight:"+jsx3.gui.Block.FONTBOLD+";";};f.g0=function(){return "font-size:"+(this.getFontSize()?this.getFontSize():a.DEFAULTFONTSIZE)+"px;";};f.eQ=function(){return "text-align:"+(this.getTextAlign()?this.getTextAlign():this.getType()==a.TYPECAPTION?jsx3.gui.Block.ALIGNRIGHT:jsx3.gui.Block.ALIGNLEFT)+";";};f.AN=function(){return this.getText()?this.getText():"";};f.beep=function(){jsx3.gui.F9(this.getRendered(),{filter:"none",backgroundColor:"#FFFFFF",backgroundImage:""});};a.getVersion=function(){return "2.4.00";};});jsx3.WindowBar=jsx3.gui.WindowBar;
