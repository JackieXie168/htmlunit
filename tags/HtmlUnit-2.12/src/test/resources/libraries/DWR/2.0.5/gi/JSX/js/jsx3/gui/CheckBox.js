/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.CheckBox",jsx3.gui.Block,[jsx3.gui.Form],function(n,f){n.UNCHECKED=0;n.CHECKED=1;n.PARTIAL=2;n.DEFAULTCLASSNAME="jsx30checkbox";f.init=function(c,h,q,a,l,g,s){this.jsxsuper(c,h,q,a,l,g);this.setDefaultChecked(s==null?n.CHECKED:s);this.jsxchecked=s;};f.getDefaultChecked=function(){return this.jsxdefaultchecked;};f.setDefaultChecked=function(j){this.jsxdefaultchecked=j;return this;};f.getChecked=function(){return this.jsxchecked!=null?this.jsxchecked:this.getDefaultChecked();};f.setChecked=function(g){if(this.jsxchecked!=g){this.jsxchecked=g;this.Ri();if(this.isOldEventProtocol())this.doEvent(jsx3.gui.Interactive.TOGGLE,{intCHECKED:g});}return this;};f.IU=function(j,c){if(!j.leftButton()&&j.isMouseEvent())return;if(this.getEnabled()==jsx3.gui.Form.STATEENABLED){var qc=this.getChecked()==n.CHECKED?n.UNCHECKED:n.CHECKED;this.jsxchecked=qc;this.Ri(c);this.doEvent(jsx3.gui.Interactive.TOGGLE,{objEVENT:j,intCHECKED:qc});}};f.Ri=function(k){if(k==null)k=this.getRendered();if(k!=null){jsx3.html.selectSingleElm(k,0,0,0).checked=this.jsxchecked==n.CHECKED;jsx3.html.selectSingleElm(k,0,0,1).style.visibility=this.jsxchecked==n.PARTIAL?"visible":"hidden";}};f.DY=function(k,b){if(k.enterKey()){this.IU(k,b);k.cancelAll();}};n.s5={};n.s5[jsx3.gui.Event.CLICK]=true;n.s5[jsx3.gui.Event.KEYDOWN]=true;f.k7=function(a,k,d){this.B_(a,k,d,3);};f.T5=function(q){if(this.getParent()&&(q==null||isNaN(q.parentwidth)||isNaN(q.parentheight))){q=this.getParent().IO(this);}else{if(q==null){q={};}}var W=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==jsx3.gui.Block.RELATIVE);var zc,tc,U,D;if(q.tagname==null)q.tagname="span";if((zc=this.getBorder())!=null&&zc!="")q.border=zc;if(W&&(tc=this.getMargin())!=null&&tc!="")q.margin=tc;if(q.boxtype==null)q.boxtype=W?"relativebox":"box";if(q.left==null)q.left=!W&&!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;if(q.top==null)q.top=!W&&!jsx3.util.strEmpty(this.getTop())?this.getTop():0;if(q.height==null)q.height=(U=this.getHeight())!=null?U:18;if(q.width==null){if((D=this.getWidth())!=null)q.width=D;}var zb=new jsx3.gui.Painted.Box(q);var Nc={};Nc.tagname="div";Nc.boxtype="inline";var ib=new jsx3.gui.Painted.Box(Nc);zb.W8(ib);var Nc={};Nc.tagname="span";Nc.boxtype="box";Nc.width=16;Nc.parentheight=zb.P5();Nc.height=18;Nc.left=0;Nc.top=0;var yc=new jsx3.gui.Painted.Box(Nc);ib.W8(yc);var Nc={};Nc.tagname="input[checkbox]";Nc.empty=true;Nc.omitpos=true;var Ub=new jsx3.gui.Painted.Box(Nc);yc.W8(Ub);var Nc={};Nc.tagname="span";Nc.boxtype="box";Nc.left=3;Nc.top=7;Nc.width=7;Nc.height=2;var rb=new jsx3.gui.Painted.Box(Nc);yc.W8(rb);var Nc={};Nc.tagname="span";Nc.boxtype="inline";Nc.top=2;Nc.parentheight=zb.P5();Nc.height=18;Nc.padding="0 0 0 18";var Dc=new jsx3.gui.Painted.Box(Nc);zb.W8(Dc);return zb;};f.paint=function(){this.applyDynamicProperties();var Zb=this.getEnabled()==jsx3.gui.Form.STATEENABLED;var B=Zb?this.lM(n.s5,0):"";var uc=this.renderAttributes(null,true);var gb=this.getChecked()==n.PARTIAL?"visible":"hidden";var hb=Zb?"":"background-color:#999999;";var ec=this.RL(true);ec.setAttributes(" id=\""+this.getId()+"\" label=\""+this.getName()+"\" class=\""+this.CH()+"\" unselectable=\"on\" "+this.vH()+this.WP()+B+uc);ec.setStyles((ec.KZ()?"overflow-x:hidden;":"")+this.I6()+this.oY()+this.g0()+this.D6()+this.QP()+this.UZ()+this.T1()+this.MU()+this.d9()+this.iN());var P=ec.pQ(0);var V=P.pQ(0);V.setAttributes(" class=\"jsx30checkbox_tristate\" ");var T=V.pQ(0);T.setAttributes(" type=\"checkbox\" name=\""+this.getName()+"\" "+this.WP()+this.by()+this.CI());var Hb=V.pQ(1);Hb.setAttributes(" class=\"jsx30checkbox_partial\" ");Hb.setStyles("visibility:"+gb+";"+hb);var Nb=ec.pQ(1);Nb.setAttributes(" unselectable=\"on\" ");return ec.paint().join(P.paint().join(V.paint().join(T.paint().join("")+Hb.paint().join("&#160;"))+Nb.paint().join(this.AN())));};f.I6=function(){return this.getCursor()==null?"cursor:pointer;":"cursor:"+this.getCursor()+";";};f.by=function(){return this.getChecked()==n.CHECKED?" checked=\"checked\" ":"";};f.CH=function(){var J=this.getClassName();return n.DEFAULTCLASSNAME+(J?" "+J:"");};f.doValidate=function(){this.setValidationState(this.getRequired()==jsx3.gui.Form.OPTIONAL||this.getChecked()==n.CHECKED?jsx3.gui.Form.STATEVALID:jsx3.gui.Form.STATEINVALID);return this.getValidationState();};n.getVersion=function(){return "3.0.00";};f.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};f.emInit=function(l){this.jsxsupermix(l);this.subscribe(jsx3.gui.Interactive.TOGGLE,this,"Ox");};f.emSetValue=function(m){this.jsxchecked=Number(m)==n.CHECKED?n.CHECKED:n.UNCHECKED;};f.emGetValue=function(){var O=this.emGetSession();if(O)return O.column.getValueForRecord(O.recordId);return null;};f.emBeginEdit=function(h,g,l,m,q,p,e){var cc=jsx3.html.selectSingleElm(e,0,0,0,0,0);if(cc&&!cc.getAttribute("disabled")){this.jsxsupermix(h,g,l,m,q,p,e);cc.focus();}else{return false;}};f.emPaintTemplate=function(){this.jsxchecked=n.UNCHECKED;this.setText("");this.setEnabled(jsx3.gui.Form.STATEDISABLED);var pc=this.paint();this.setEnabled(jsx3.gui.Form.STATEENABLED);var sc=this.paint();var Q=this.PS(sc,pc);Q=Q.replace(/<(input .*?)\/>/g,"<$1><xsl:if test=\"{0}='1'\"><xsl:attribute name=\"checked\">checked</xsl:attribute></xsl:if></input>");return Q;};f.Ox=function(k){var Jc=this.emGetSession();if(Jc){var rb=k.context.intCHECKED;this.jsxchecked=rb;Jc.column.setValueForRecord(Jc.recordId,rb.toString());}};});jsx3.CheckBox=jsx3.gui.CheckBox;
