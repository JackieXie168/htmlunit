/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Block","jsx3.gui.Column");jsx3.Class.defineClass("jsx3.gui.List",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(h,a){var Eb=jsx3.gui.Event;var Jb=jsx3.gui.Interactive;var rb=jsx3.xml.CDF;var lc=jsx3.util.Logger.getLogger(h.jsxclass.getName());h.DEFAULTXSLURL=jsx3.resolveURI(jsx3.CLASS_LOADER.resolvePath("jsx:///xsl/@path@/jsx30list.xsl"));h.SELECTBGIMAGE="url("+jsx3.resolveURI("jsx:///images/list/select.gif")+")";h.DEFAULTBACKGROUNDHEAD="background-image:url("+jsx3.resolveURI("jsx:///images/list/header.gif")+");";jsx3.html.loadImages("jsx:///images/list/select.gif","jsx:///images/list/header.gif");h.DEFAULTBACKGROUNDCOLOR="#F3F2F4";h.DEFAULTBACKGROUNDCOLORHEAD="#c8cfd8";h.SORTASCENDING="ascending";h.SORTDESCENDING="descending";h.DEFAULTHEADERHEIGHT=20;h.MULTI=1;h.SINGLE=0;h.NOTSELECTABLE=2;h.RESIZEBARBGCOLOR="#2050df";h.DEFAULTROWCLASS="jsx30list_r1";a.init=function(p){this.jsxsuper(p);this._jsxij=null;this._jsxXG=null;this._jsxPE=null;this._jsxvl=null;};a.onRemoveChild=function(l,r){this.jsxsuper(l,r);this.resetXslCacheData();this.repaint();};a.paintChild=function(j,r){if(!r){this.resetXslCacheData();this.repaint();}};a.getXSL=function(d){var vb=this.jsxsupermix(true);if(vb==null)vb=this.getServer().getCache().getOrOpenDocument(d||h.DEFAULTXSLURL,this.getXSLId());if(vb!=null){vb.setSelectionNamespaces("xmlns:xsl='http://www.w3.org/1999/XSL/Transform'");var ac=vb.selectSingleNode("//xsl:comment[.='JSXUNCONFIGURED']");if(ac!=null){ac.setValue("JSXCONFIGURED");var V=this.LC();var J=V.length;if(J>0){for(var kc=0;kc<=J;kc++){var Sb=new jsx3.xml.Document();var _=kc==J?V[J-1].paintXSLString(true):V[kc].paintXSLString();Sb.loadXML(_);if(Sb.hasError()){lc.error("Error loading XSL for column #"+kc+" of "+this+": "+Sb.getError().description);}else{ac.getParent().insertBefore(Sb.getRootNode(),ac);}}}}}else{jsx3.util.Logger.doLog("XLST","No default stylesheet can be found for the List control, "+this.getId()+".");}return vb;};a.clearXSL=function(){this.resetXslCacheData();return this;};h.Zw=function(k){return k&&k.getDisplay()!=jsx3.gui.Block.DISPLAYNONE;};a.LC=function(){return this.getChildren().filter(h.Zw);};a.doValidate=function(){var Ab=this.getSelectedNodes().getLength()>0||this.getRequired()==jsx3.gui.Form.OPTIONAL;this.setValidationState(Ab?jsx3.gui.Form.STATEVALID:jsx3.gui.Form.STATEINVALID);return this.getValidationState();};a.getResizable=function(){return this.jsxresize==null?jsx3.Boolean.TRUE:this.jsxresize;};a.setResizable=function(s){this.jsxresize=s;return this;};a.DA=function(q,o){if(!q.leftButton())return;Eb.publish(q);if(this.getCanResize()!=0){var wc=o;var Wb=this.getRendered().childNodes[1];var uc=parseInt(wc.parentNode.parentNode.parentNode.parentNode.parentNode.style.left);var Dc=wc.parentNode.parentNode.offsetLeft+uc;Wb.style.left=Dc+"px";this._jsxPE=Dc;this._jsxvl=wc.parentNode.parentNode.cellIndex;var cb=this._jsxvl-1;var Pb=this.doEvent(Jb.BEFORE_RESIZE,{objEVENT:q,intINDEX:cb,intCOLUMNINDEX:cb})===false;if(!Pb){Wb.style.visibility="visible";jsx3.gui.Event.subscribe(jsx3.gui.Event.MOUSEUP,this,"wp");jsx3.gui.Interactive.P3(q,Wb,false,true);}else{Wb.style.left="-10px";}}q.cancelReturn();q.cancelBubble();};a.wp=function(d,r){var d=d.event;jsx3.gui.Event.unsubscribe(jsx3.gui.Event.MOUSEUP,this,"wp");if(r==null)r=this.getRendered().childNodes[1];d.releaseCapture(r);var Cb=parseInt(r.style.left);var hc=Cb-this._jsxPE;var Ob=this._jsxvl-1;var mb=this.LC();var zc=this.getDocument();var wc=zc.getElementById(this.getId()+"_jsxhead");var J=wc.childNodes[0].childNodes[0].childNodes[0].childNodes[Ob];var pc=J.offsetWidth;var vc=mb[Ob].getWidth()+"";var rc;if(rc=vc.indexOf("%")>-1){var sb=this.getAbsolutePosition().W;var Fc=pc+hc;Fc=parseInt(Fc/sb*1000)/10;if(Fc<2)Fc=2;Fc=Fc+"%";}else{var Fc=pc+hc;if(Fc<4)Fc=4;}r.style.left="-10px";var kb={objEVENT:d,intDIFF:hc,intINDEX:Ob,intOLDWIDTH:pc,vntWIDTH:Fc,intCOLUMNINDEX:Ob};var dc=this.doEvent(Jb.AFTER_RESIZE,kb);if(!(dc===false)){var Mb=dc instanceof Object&&dc.vntWIDTH?dc.vntWIDTH:Fc;mb[Ob].setWidth(Mb);this.resetXslCacheData();this.repaintBody();this.repaintHead();}};a.Oh=function(o,q){if(!o.leftButton())return;Eb.publish(o);var Zb=q;var oc=this.getDocument().getElementById(this.getId()+"_jsxghost");oc.innerHTML="";oc.innerText="";var qc=Zb.childNodes[0].cloneNode(true);var V=parseInt(Zb.parentNode.parentNode.parentNode.style.left);var P={};P.boxtype="box";P.tagname="div";P.left=Zb.offsetLeft+V;P.top=0;P.parentheight=Zb.offsetHeight;P.parentwidth=parseInt(Zb.offsetWidth);P.width="100%";P.height="100%";P.border="solid 1px #ffffff;solid 1px #9898a5;solid 1px #9898a5;solid 1px #ffffff";P.padding=parseInt(Zb.childNodes[0].offsetTop)+" "+(Zb.style.paddingRight?parseInt(Zb.style.paddingRight):0)+" "+(Zb.style.paddingBottom?parseInt(Zb.style.paddingBottom):0)+" "+(Zb.style.paddingLeft?parseInt(Zb.style.paddingLeft):0);var mc=new jsx3.gui.Painted.Box(P);oc.style.left=mc.O8()+"px";oc.style.top=mc.hH()+"px";oc.style.width=mc.KZ()+"px";oc.style.height=mc.wI()+"px";oc.style.fontName=Zb.style.fontName;oc.style.fontSize=Zb.style.fontSize;oc.style.textAlign=Zb.style.textAlign;oc.style.fontWeight=Zb.style.fontWeight;oc.style.visibility="visible";jsx3.gui.Painted.HM(oc,mc.N4(),"padding");oc.appendChild(qc);this._jsxPE=Zb.offsetLeft+V;this._jsxvl=Zb.cellIndex;jsx3.gui.Event.subscribe(jsx3.gui.Event.MOUSEUP,this,"vi");jsx3.gui.Interactive.P3(o,oc,false,true);};a.Aq=function(n,r){var Vb=n.getType()==jsx3.gui.Event.CLICK?r.cellIndex:this._jsxvl;var Fb=this.LC()[Vb];var vb=this.getChildren().indexOf(Fb);if(this.getCanSort()!=0&&Fb!=null&&Fb.getCanSort()!=0){var Db={objEVENT:n,intCOLUMNINDEX:vb};var wb=this.doEvent(Jb.BEFORE_SORT,Db);if(wb!==false){var fb=wb instanceof Object&&wb.intCOLUMNINDEX!=null?wb.intCOLUMNINDEX:vb;this.Wm(n,fb);}}};a.vi=function(l,n){var l=l.event;jsx3.gui.Event.unsubscribe(jsx3.gui.Event.MOUSEUP,this,"vi");var fb=this.getDocument().getElementById(this.getId()+"_jsxghost");if(n==null)var n=fb;l.releaseCapture(fb);fb.style.visibility="hidden";if(this._jsxPE==parseInt(fb.style.left)){this.Aq(l,n);}else{if(this.getCanReorder()!=0){var ec=parseInt(fb.style.left);var Bb=this.LC();var yb=this.getChildren().length;var wb=Bb[0].getRendered().parentNode;var W=this.getChildren().indexOf(Bb[this._jsxvl]);var vc=0;for(var sb=0;sb<yb;sb++){if(h.Zw(this.getChild(sb))){var ab=wb.childNodes[vc].offsetLeft;if(ec<ab){if(W!=sb)this.Gv(W,sb-1);jsx3.EventHelp.reset();return;}vc++;}}if(W!=yb-1)this.Gv(W,yb-1);}}};a.setSortColumn=function(m){this.Wm(this.isOldEventProtocol(),m);};a.Wm=function(o,m){this.jsxsortcolumn=m;this.doSort();if(o){this.doEvent(Jb.AFTER_SORT,{objEVENT:o instanceof jsx3.gui.Event?o:null,intCOLUMNINDEX:m});}return this;};a.getSortColumn=function(){return this.jsxsortcolumn;};a.cu=function(){var bc=this.jsxsortcolumn!=null?this.getChild(this.jsxsortcolumn)==null?null:this.getChild(this.jsxsortcolumn).getSortPath():this.getSortPath();return bc?bc.substring(1):"";};a.Gv=function(k,o){var O=this.getChildren();var ub=O.length;var oc=this.getChild(k);if(o<k){for(var x=k;x>o;x--){if(x>0)O[x]=O[x-1];}O[o+1]=oc;}else{for(var x=k;x<=o;x++){if(x<=ub-2)O[x]=O[x+1];}O[o]=oc;}this.resetXslCacheData();this.repaint();this.getServer().getDOM().onChange(jsx3.app.DOM.TYPEADD,this.getId(),this.getChild(0).getId());};a.doSort=function(c){if(c!=null){this.setSortDirection(c);}else{this.setSortDirection(this.getSortDirection()==h.SORTASCENDING?h.SORTDESCENDING:h.SORTASCENDING);}this.repaintBody();this.repaintHead();};a.getSortPath=function(){return this.jsxsortpath==null?"":this.jsxsortpath;};a.setSortPath=function(p){this.jsxsortpath=p;return this;};a.getSortDirection=function(){return this.jsxsortdirection==null?h.SORTASCENDING:this.jsxsortdirection;};a.setSortDirection=function(p){this.jsxsortdirection=p;return this;};a.getMultiSelect=function(){return this.jsxmultiselect==null?h.MULTI:this.jsxmultiselect;};a.setMultiSelect=function(r){this.jsxmultiselect=r;if(r==h.NOTSELECTABLE)this.deselectAllRecords();return this;};a.getCanReorder=function(){return this.jsxreorder==null?1:this.jsxreorder;};a.setCanReorder=function(j){this.jsxreorder=j;return this;};a.getCanSort=function(){return this.jsxsort==null?1:this.jsxsort;};a.setCanSort=function(s){this.jsxsort=s;return this;};a.getBackgroundColorHead=function(){return this.jsxbgcolorhead;};a.setBackgroundColorHead=function(i){this.jsxbgcolorhead=i;return this;};a.getBackgroundHead=function(){return this.jsxbghead;};a.setBackgroundHead=function(f){this.jsxbghead=f;return this;};a.getHeaderHeight=function(){return this.jsxheaderheight;};a.setHeaderHeight=function(p){this.jsxheaderheight=p;return this;};h.rm=function(k,o,i,s){return "<span id='JSX' style='font-family:Verdana;font-size:10px;padding:0px;height:22px;width:200px;overflow:hidden;text-overflow:ellipsis;filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr=#eedfdfe8, EndColorStr=#00ffffff);'><table style='font-family:verdana;font-size:10px;color:#a8a8a8;' cellpadding='3' cellspacing='0'>"+jsx3.html.getOuterHTML(k).doReplace("id=","tempid=").doReplace("BACKGROUND","bg").doReplace("class=","jsxc=")+"</table></span>";};h.doBlurItem=function(s){var A=s.getAttribute("JSXDragId");var y=s.id.substring(0,s.id.length-(A.length+1));var Ub=jsx3.GO(y);s.style.fontWeight="normal";if(Ub!=null)Ub._jsxij=null;};h.doFocusItem=function(m){var wc=m.getAttribute("JSXDragId");var ac=m.id.substring(0,m.id.length-(wc.length+1));var W=jsx3.GO(ac);if(W==null){lc.warn("No list with id "+ac+".");return;}if(!W.g5(wc))return;m.focus();m.style.fontWeight="bold";W._jsxij=m;};a.mL=function(n,r){if(this.getCanDrag()==1&&n.leftButton()){var nb=n.srcElement();var Bc=this.it(nb);if(Bc[0]!=null){if(!this.g5(Bc[0]))return;Eb.publish(n);this.doDrag(n,Bc[2],h.rm,{strRECORDID:Bc[0],intCOLUMNINDEX:Bc[1]});}}};a._4=function(j,c){var ob=this.it(j.srcElement());if(this.getCanDrop()==1&&jsx3.EventHelp.isDragging()&&jsx3.EventHelp.JSXID!=this){if(jsx3.EventHelp.DRAGTYPE=="JSX_GENERIC"){var sc=jsx3.EventHelp.JSXID.getId();var Ob=jsx3.EventHelp.DRAGID;var t=jsx3.GO(sc);if(t==null)return;var Ub=jsx3.gui.isMouseEventModKey(j);var cb=t.doEvent(Jb.ADOPT,{objEVENT:j,strRECORDID:Ob,objTARGET:this,bCONTROL:Ub});var Rb={objEVENT:j,objSOURCE:t,strDRAGID:Ob,strDRAGTYPE:jsx3.EventHelp.DRAGTYPE,strRECORDID:ob[0],intCOLUMNINDEX:ob[1],bALLOWADOPT:cb!==false};var A=this.doEvent(Ub?Jb.CTRL_DROP:Jb.DROP,Rb);if(cb!==false&&A!==false&&t.instanceOf(jsx3.xml.CDF))this.adoptRecord(t,Ob);}}else{if(j.rightButton()){var Sb;if((Sb=this.getMenu())!=null){var Hc=this.getServer().getJSX(Sb);if(Hc!=null){var oc={objEVENT:j,objMENU:Hc,strRECORDID:ob[0],intCOLUMNINDEX:ob[1]};var Vb=this.doEvent(Jb.MENU,oc);if(Vb!==false){if(Vb instanceof Object&&Vb.objMENU instanceof jsx3.gui.Menu)Hc=Vb.objMENU;Hc.showContextMenu(j,this,ob[0]);}}}}}};a.it=function(l){var jc=null;var _b=null;var Kc=null;while(l!=null&&jc==null){if(l.tagName&&l.tagName.toLowerCase()=="td")_b=l.getAttribute("cellIndex");else{if(l.tagName&&l.tagName.toLowerCase()=="tr"){jc=l.getAttribute("JSXDragId");Kc=l;}}l=l.parentNode;}return [jc,jc?_b:null,Kc];};a.dZ=function(i,s,k){if(!this.g5(s.getAttribute("JSXDragId")))return;if(!this.g5(k.getAttribute("JSXDragId")))return;if(this.getMultiSelect()==h.NOTSELECTABLE)return;var eb=0;var M=this.getDocument().getElementById(this.getId()+"_jsxbody").childNodes[0].childNodes[0];var Fb=[],nb=[];for(var O=M.childNodes.length-1;O>=0;O--){if(M.childNodes[O]==k)eb++;if(M.childNodes[O]==s)eb++;if(eb>=1&&eb<=2){var tb=M.childNodes[O];Fb.push(tb.getAttribute("JSXDragId"),tb);}if(eb==2)break;}this.CB(i,Fb,nb,false);h.doFocusItem(s);};a.DY=function(f,g){if(this.jsxsupermix(f,g))return;if(this._jsxij==null)return;var ic=this.getId().length;var Mc=f.keyCode();var u=this.getMultiSelect()==1;var Cc=false;var Z=jsx3.gui.isMouseEventModKey(f);if(Mc==Eb.KEY_ARROW_UP){if(this._jsxij==this._jsxij.parentNode.firstChild)return;if(u&&Z){h.doFocusItem(this._jsxij.previousSibling);}else{if(u&&f.shiftKey()){this.dZ(f,this._jsxij.previousSibling,this._jsxXG);}else{var L=this._jsxij.previousSibling;this.zf(f,L.getAttribute("JSXDragId"),L,false);}}Cc=true;}else{if(Mc==Eb.KEY_ARROW_DOWN){if(this._jsxij==this._jsxij.parentNode.lastChild)return;if(u&&Z){h.doFocusItem(this._jsxij.nextSibling);}else{if(u&&f.shiftKey()){this.dZ(f,this._jsxij.nextSibling,this._jsxXG);}else{var L=this._jsxij.nextSibling;this.zf(f,L.getAttribute("JSXDragId"),L,false);}}Cc=true;}else{if(Mc==Eb.KEY_ENTER){this.Oy(f);Cc=true;}else{if(Mc==Eb.KEY_SPACE){if(u&&Z){var Fc=this._jsxij.getAttribute("JSXDragId");if(this.isSelected(Fc)){this.Ez(f,Fc,this._jsxij);}else{this.zf(f,Fc,this._jsxij,true);}}else{if(u&&f.shiftKey()){this.dZ(f,f.srcElement(),this._jsxXG);}else{var L=this._jsxij;this.zf(f,L.getAttribute("JSXDragId"),L,false);}}Cc=true;}else{if(Mc==Eb.KEY_TAB&&f.shiftKey()){this.focus();Cc=true;}else{if(Mc==Eb.KEY_TAB){this.getRendered().lastChild.focus();Cc=true;}}}}}}if(Cc)f.cancelAll();};a.IU=function(j,c){var O=false;var Mc=j.srcElement();if(Mc.tagName&&Mc.tagName.toLowerCase()=="tbody"||Mc==c){this.PC(j,this.getSelectedIds(),[]);return;}var Wb=this.getRendered();while(jsx3.util.strEmpty(Mc.getAttribute("JSXDragId"))&&Mc!=Wb)Mc=Mc.parentNode;if(!Mc||!Mc.getAttribute("JSXDragId")){this.PC(j,this.getSelectedIds(),[]);return;}var ac=this.getMultiSelect()==h.MULTI;var kb=jsx3.gui.isMouseEventModKey(j);if(ac&&j.shiftKey()&&this._jsxXG!=null){this.dZ(j,Mc,this._jsxXG);O=true;}else{if(ac&&kb){var Bc=Mc.getAttribute("JSXDragId");if(this.isSelected(Bc)){this.Ez(j,Bc,Mc);}else{this.zf(j,Bc,Mc,true);h.doFocusItem(Mc);}O=true;}else{if(this.isSelected(Mc.getAttribute("JSXDragId"))){if(kb||j.shiftKey()){this.Ez(j,Mc.getAttribute("JSXDragId"),Mc);}}else{this.zf(j,Mc.getAttribute("JSXDragId"),Mc,false);}O=true;}}if(O){j.cancelBubble();j.cancelReturn();}};a.LH=function(b,k){this.Oy(b);};a.executeRecord=function(f){var Kc=this.getRecordNode(f);if(Kc!=null)this.eval(Kc.getAttribute("jsxexecute"),{strRECORDID:f});};a.doExecute=function(c){this.Oy(this.isOldEventProtocol(),c!=null?[c]:null);};a.Oy=function(l,b){if(b==null)b=this.getSelectedIds();for(var y=0;y<b.length;y++){var tb=b[y];var Pb=this.getRecordNode(tb);if(Pb.getAttribute(rb.ATTR_UNSELECTABLE)=="1")continue;this.eval(Pb.getAttribute("jsxexecute"),{strRECORDID:tb});}if(b.length>0&&l)this.doEvent(Jb.EXECUTE,{objEVENT:l instanceof Eb?l:null,strRECORDID:b[0],strRECORDIDS:b});};a.isSelected=function(j){return this.getXML().selectSingleNode("//record[@jsxid='"+j+"' and @"+rb.ATTR_SELECTED+"='1']")!=null;};a.doSelect=function(d,c,s,b){this.zf(!s&&this.isOldEventProtocol(),d,null,true);if(d&&b)this.revealRecord(d);return this;};a.selectRecord=function(r){if(!this.g5(r))return;if(this.getMultiSelect()==h.NOTSELECTABLE)return;this.zf(false,r,null,true);return this;};a.deselectRecord=function(l){this.Ez(false,l,null);return this;};a.deselectAllRecords=function(){this.PC(false,this.getSelectedIds(),[]);return this;};a.zf=function(k,o,b,l){var ic=this.getRecordNode(o);if(!ic||ic.getAttribute(rb.ATTR_SELECTED)=="1"||ic.getAttribute(rb.ATTR_UNSELECTABLE)=="1"||this.getMultiSelect()==h.NOTSELECTABLE)return false;var pb=l&&this.getMultiSelect()==h.MULTI;if(!pb)this.deselectAllRecords();ic.setAttribute(rb.ATTR_SELECTED,"1");b=b||this.uD(o);if(b!=null){if(!pb){this._jsxXG=b;h.doFocusItem(b);}b.style.backgroundImage=h.SELECTBGIMAGE;}if(k){this.doEvent(Jb.SELECT,{objEVENT:k instanceof Eb?k:null,strRECORDID:o,strRECORDIDS:[o]});this.doEvent(Jb.CHANGE,{objEVENT:k instanceof Eb?k:null});}return true;};a.CB=function(d,j,l,e){if(!e)this.deselectAllRecords();for(var R=0;R<j.length;R++){var ec=this.zf(false,j[R],l[R],true);if(!ec){j.splice(R,1);l.splice(R,1);R--;}}if(d&&j.length>0){this.doEvent(Jb.SELECT,{objEVENT:d,strRECORDID:j[0],strRECORDIDS:j});this.doEvent(Jb.CHANGE,{objEVENT:d});}};a.Ez=function(k,o,b){var ub=this.getRecordNode(o);if(!ub||ub.getAttribute(rb.ATTR_SELECTED)!="1")return false;ub.removeAttribute(rb.ATTR_SELECTED);b=b||this.uD(o);if(b!=null){if(this._jsxXG==b){delete this._jsxXG;h.doBlurItem(b);}b.style.backgroundImage="";}if(k){this.doEvent(Jb.SELECT,{objEVENT:k instanceof Eb?k:null,strRECORDID:null,strRECORDIDS:[]});this.doEvent(Jb.CHANGE,{objEVENT:k instanceof Eb?k:null});}return true;};a.PC=function(l,b,d){for(var t=0;t<b.length;t++){var ic=this.Ez(false,b[t],d[t]);if(!ic){b.splice(t,1);d.splice(t,1);t--;}}if(l&&b.length>0){this.doEvent(Jb.SELECT,{objEVENT:l,strRECORDID:b[0],strRECORDIDS:b});this.doEvent(Jb.CHANGE,{objEVENT:l});}};a.focusRecord=function(j){var ib=this.uD(j);if(ib!=null)ib.focus();return this;};a.doDeselect=function(o,k){this.Ez(this.isOldEventProtocol(),o,null);return this;};a.getActiveRow=function(){return this._jsxXG;};a.revealRecord=function(m,n){var ic=this.uD(m);if(ic){var F=n?n.getRendered():this.getRendered();if(F)jsx3.html.scrollIntoView(ic,F,0,10);}};a.uD=function(j){var Ib=this.getDocument();return Ib!=null?Ib.getElementById(this.getId()+"_"+j):null;};a.redrawRecord=function(e,p){if(p==jsx3.xml.CDF.INSERT){this.appendRow(this.getRecord(e),e);}else{if(e!=null&&p==jsx3.xml.CDF.DELETE){var D;if((D=this.uD(e))!=null){D.parentNode.removeChild(D);}}else{if(e!=null&&p==jsx3.xml.CDF.UPDATE){this.updateRow(e);}}}};a.getSelectedNodes=function(){return this.getXML().selectNodes("//record[@"+rb.ATTR_SELECTED+"='1']");};a.getSelectedIds=function(){var S=this.getSelectedNodes();var Ac=[];var kc=S.getLength();for(var Cb=0;Cb<kc;Cb++){Ac[Cb]=S.getItem(Cb).getAttribute("jsxid");}return Ac;};a.getValue=function(){var uc=this.getSelectedIds();return this.getMultiSelect()==h.MULTI?uc:uc[0];};a.setValue=function(c){if(c instanceof Array){if(this.getMultiSelect()!=h.MULTI)throw new jsx3.IllegalArgumentException("strRecordId",c);}else{c=c!=null?[c]:[];}this.CB(false,c,[],false);return this;};a.CL=function(i,d){if(this.getCanSpy()==1&&this.getEvent(Jb.SPYGLASS)){var sc=this.it(i.srcElement());if(sc[0]){h._curSpyRow=sc[2];this.applySpyStyle(sc[2]);var U=i.clientX()+jsx3.EventHelp.DEFAULTSPYLEFTOFFSET;var oc=i.clientY()+jsx3.EventHelp.DEFAULTSPYTOPOFFSET;var A=this;i._0();h.ri=window.setTimeout(function(){if(A._jsxon)return;var Y={objEVENT:i,strRECORDID:sc[0],intCOLUMNINDEX:sc[1]};var Kb=A.doEvent(Jb.SPYGLASS,Y);if(Kb){jsx3.gui.Interactive.hideSpy();A.showSpy(Kb,U,oc);}},jsx3.EventHelp.SPYDELAY);}}};a.u2=function(){if(h._curSpyRow){this.removeSpyStyle(h._curSpyRow);delete h._curSpyRow;}window.clearTimeout(h.ri);jsx3.gui.Interactive.hideSpy();};h.s5={};h.s5[Eb.MOUSEOVER]=true;h.s5[Eb.MOUSEOUT]=true;h.s5[Eb.CLICK]=true;h.s5[Eb.DOUBLECLICK]=true;h.s5[Eb.KEYDOWN]=true;h.s5[Eb.MOUSEDOWN]=true;h.s5[Eb.MOUSEUP]=true;a.k7=function(p,m,b){var zc=this.getDocument();if(zc!=null){var Ib=zc.getElementById(this.getId()+"_jsxbody");if(Ib!=null){var E=this.getHeaderHeight()!=null?this.getHeaderHeight():h.DEFAULTHEADERHEIGHT;Ib.style.height=Math.max(0,p.parentheight-E)+"px";}}};a.paint=function(){this.applyDynamicProperties();this._jsxXG=null;var Tb=this.getParent().IO(this);var rc=this.getId();var z="";var jc="<span"+this.RX(Eb.MOUSEDOWN,"DA",1)+" id=\""+rc+"_jsxcolresize\" style=\"background-color:"+h.RESIZEBARBGCOLOR+";\" class=\"jsx30list_colresize\"></span>";var J=this.getHeaderHeight()!=null?this.getHeaderHeight():h.DEFAULTHEADERHEIGHT;var ec="<div id=\""+rc+"\" class=\"jsx30list\" "+"style=\""+""+this.MU()+this.T1()+this.A0()+this.UZ()+this.K2()+"\" label=\""+this.getName()+"\""+this.renderAttributes()+">";ec=ec+"<table class=\"jsx30list_table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"\">";if(J>0){ec=ec+("<tr><td height=\""+J+"\" style=\"position:relative;overflow:hidden;\">");ec=ec+("<div id=\""+rc+"_jsxhead\" class=\"jsx30list_headspan\" style=\""+this.CN()+this.fL()+"\">");ec=ec+this.m0();ec=ec+"</div>";ec=ec+("<div id=\""+rc+"_jsxghost\""+" class=\"jsx30list_ghost\">&#160;</div>");ec=ec+"</td></tr>";z=" onscroll=\"this.parentNode.parentNode.parentNode.childNodes[0].childNodes[0].childNodes[0].childNodes[0].style.left = -this.scrollLeft + 'px';\" ";}var y=this.lM(h.s5,5);var Kc=Tb.parentheight-J;ec=ec+("<tr><td height=\""+(J==0?"100%":Kc)+"\" valign=\"top\" style=\"position:relative;height:"+Kc+"px"+"\">");ec=ec+("<div id=\""+rc+"_jsxbody\""+y+z+" style=\""+this.UZ()+this.getBorder()+";height:"+Kc+"px"+"\" class=\"jsx30list_bodyspan\">");ec=ec+this.PR();ec=ec+this.S3();ec=ec+"</div>";ec=ec+"</td></tr>";ec=ec+"</table>";ec=ec+jc;ec=ec+"</div>";return ec;};a.UZ=function(){return "background-color:"+(this.getBackgroundColor()?this.getBackgroundColor():h.DEFAULTBACKGROUNDCOLOR)+";";};a.K2=function(){return this.getBackground()?this.getBackground()+";":"";};a.CN=function(){return "background-color:"+(this.getBackgroundColorHead()?this.getBackgroundColorHead():h.DEFAULTBACKGROUNDCOLORHEAD)+";";};a.fL=function(){return this.getBackgroundHead()?this.getBackgroundHead()+";":h.DEFAULTBACKGROUNDHEAD;};a.CH=function(){return this.getClassName()?this.getClassName():h.DEFAULTROWCLASS;};a.nV=function(){return "height:"+(this.getHeaderHeight()?this.getHeaderHeight():h.DEFAULTHEADERHEIGHT)+"px;";};a.S3=function(){return "";};a.m0=function(){var Mc="<table cellspacing=\"0\" cellpadding=\"3\" border=\"0\" style=\"position:absolute;left:0px;top:0px;table-layout:fixed;"+this.nV()+"\">";Mc=Mc+("<tr style=\""+this.nV()+"\" "+this.vH()+">");var O=this.LC();var z=this.getChildren();var yb=z.length;var E=0;for(var G=0;E<=O.length;G++){if(z[G]==null||h.Zw(z[G])){if(E==O.length){if(O.length>0)Mc=Mc+O[O.length-1].paint(true);}else{if(G<z.length){var Fc=G==this.getSortColumn()?this.getSortDirection():null;Mc=Mc+z[G].paint(false,Fc);}}E++;}}Mc=Mc+"</tr></table>";return Mc;};a.PR=function(){var pb="<table jsxid=\""+this.getId()+"\" cellspacing=\"0\" cellpadding=\"3\" border=\"0\" style=\"table-layout:fixed;"+this.K2()+this.g0()+this.oY()+";\">";pb=pb+this.doTransform();pb=pb+"</table>";return pb;};a.repaintBody=function(){var u=this.getDocument();if(u!=null){var ic=u.getElementById(this.getId()+"_jsxbody");if(ic!=null)jsx3.html.setOuterHTML(ic.childNodes[0],this.PR());}};a.repaintHead=function(){var x=this.getDocument();if(x!=null){var K=x.getElementById(this.getId()+"_jsxhead");if(K!=null){jsx3.html.setOuterHTML(K.childNodes[0],this.m0());this.scrollHead();}}};a.scrollHead=function(){var ib=this.getDocument();var ic=ib.getElementById(this.getId()+"_jsxbody");if(ic&&ic.scrollLeft!=0){var xb=ib.getElementById(this.getId()+"_jsxhead");if(xb!=null)xb.childNodes[0].style.left=-ic.scrollLeft+"px";}};a.doTransform=function(r){var K={};if(r)K.jsxrowid=r;K.jsxtabindex=isNaN(this.getIndex())?0:this.getIndex();K.jsxselectionbg=h.SELECTBGIMAGE;K.jsxtransparentimage=jsx3.gui.Block.SPACE;K.jsxid=this.getId();K.jsxsortpath=this.cu();K.jsxsortdirection=this.getSortDirection();K.jsxrowclass=this.CH();K.jsxsorttype=this.getSortType();var db=this.getXSLParams();for(var mc in db)K[mc]=db[mc];var I=this.jsxsupermix(K);I=I.doReplace("<[/]*JSX_FF_WELLFORMED_WRAPPER[/]*>","");return I;};h.onDelete=function(i,p){var Fc=jsx3.html.getJSXParent(p);if(Fc instanceof h)Fc.deleteRecord(i);};h.onCheck=function(l,q,m,f,b){var bc=jsx3.gui.Event.getCurrent();if(q.substring(0,1)=="@")q=q.substring(1);var Ac=jsx3.html.getJSXParent(m);if(f)m.checked=!m.checked;var db=m.checked;var M=db?b[0]:b[1];Ac.insertRecordProperty(l,q,M,false);Ac.doEvent(Jb.AFTER_EDIT,{objEVENT:bc,strATTRIBUTENAME:q,strATTRIBUTEVALUE:M,strRECORDID:l,objGUI:m,objMASK:null});if(bc)bc.cancelReturn();};h.onRadio=function(p,l,i,b,q){if(l.substring(0,1)=="@")l=l.substring(1);if(b)i.checked=true;h.onCheck(p,l,i,false,q);if(b){var hb=jsx3.html.getJSXParent(i);var fc=hb.getRecordNode(p);var uc;if(fc!=null&&(uc=fc.getAttribute("jsxgroupname"))!=null){var Vb=hb.getXML().selectNodes("//record[@jsxgroupname='"+uc+"']");for(var N=0;N<Vb.getLength();N++){var oc=Vb.getItem(N);if(oc.getAttribute("jsxid")!=p)Vb.getItem(N).setAttribute(l,q[1]);}}}};a.appendRow=function(j,m){var x=this.getDocument();if(x!=null){var V=this.getId();var xb=x.getElementById(V+"_jsxbody").childNodes[0].childNodes[0];var Ib=true;if(m==null){m=jsx3.xml.CDF.getKey();Ib=false;}if(j==null)j={jsxid:m};this.doEvent(Jb.BEFORE_APPEND,{objMASTERRECORD:j});if(Ib==false)this.insertRecord(j,null,false);var Db=h.Cr(this.doTransform(m));if(Db!=""){var ac=new jsx3.xml.Document();ac.loadXML(Db);if(!ac.hasError()){var K=x.createElement("TR");var rc=ac.getRootNode();h.Rz(rc,K);var ib=rc.selectNodes("td");for(var Mb=0;Mb<ib.getLength();Mb++){rc=ib.getItem(Mb);var pb=x.createElement("TD");h.Rz(rc,pb);K.appendChild(pb);var ab=rc.getChildNodes(true);var Z="";for(var H=0;H<ab.getLength();H++){Z=Z+ab.getItem(H).toString();}pb.innerHTML=Z;}xb.appendChild(K);this.doEvent(Jb.AFTER_APPEND,{objMASTERRECORD:j,objTR:K});}else{lc.warn("A new row could not be appended to "+this+" because of an XML error: "+ac.getError().description);}}}};a.updateRow=function(j){var Gc;if(this.getRecordNode(j)!=null&&(Gc=this.uD(j))!=null){var T=h.Cr(this.doTransform(j));if(T!=""){var yc=new jsx3.xml.Document();yc.loadXML(T);if(!yc.hasError()){var Ib=yc.getRootNode();h.Rz(Ib,Gc);var gc=Ib.selectNodes("td");for(var cc=0;cc<gc.getLength();cc++){Ib=gc.getItem(cc);var Xb=Gc.childNodes.item(cc);h.Rz(Ib,Xb);var hc=Ib.getChildNodes(true);var mb="";for(var Qb=0;Qb<hc.getLength();Qb++){mb=mb+hc.getItem(Qb).toString().doReplace("&lt;","<").doReplace("&gt;",">").doReplace("&amp;","&");}Xb.innerHTML=mb;}}else{jsx3.util.Logger.doLog("list.update.1","A row could not be updated, due to the following reasons(s): "+yc.getError().description,3,false);}}}};h.Rz=function(j,q){var cc=j.getAttributes();for(var Q=0;Q<cc.getLength();Q++){var Cc=cc.getItem(Q);var Dc=Cc.getNodeName();var I=/(on(?:mousedown|click|focus|blur|mouseup|scroll|keydown|keypress))/gi;var Gc=Cc.getValue();if(Dc.match(I)){q[Dc.toLowerCase()]=new Function(Gc);}else{if(Dc=="class"){q.className=Gc;}else{if(Dc=="style"){jsx3.gui.Painted.HM(q,Gc);}else{q.setAttribute(Dc,Gc);}}}}};h.py=function(f,m){var Fc=f.getAttributes();for(var Lb=0;Lb<Fc.getLength();Lb++){var Kc=Fc.getItem(Lb);var oc=Kc.getNodeName();var mc=/(on(?:mousedown|click|focus|blur|mouseup|scroll|mouseup|keydown|keypress))/gi;var Tb=Kc.getValue();if(oc.match(mc)){m[oc]=new Function(Tb);}else{if(oc!="class"){m.setAttribute(oc,Tb);}else{m.className=Tb;}}}if(!m.tagName||m.tagName.toLowerCase()!="tr")m.style.position="relative";};h.Cr=function(o){var Vb=/(<(?:img|input)[^>]*)(>)/gi;o=o.replace(Vb,function(n,m,l){return m+"/>";});o=o.doReplace("&nbsp;","&#160;").doReplace("&","&amp;");return o;};a.getGrowBy=function(){return this.jsxgrowby;};a.setGrowBy=function(k){this.jsxgrowby=k;return this;};a.getAutoExpand=function(){return jsx3.Boolean.valueOf(this.getGrowBy());};a.setAutoExpand=function(g){return this.setGrowBy(g?1:0);};a.getSortType=function(){if(this.jsxsorttype==null){if(this.jsxsortcolumn!=null)return this.getChild(this.jsxsortcolumn)==null?jsx3.gui.Column.TYPETEXT:this.getChild(this.jsxsortcolumn).getDataType();var mb=this.getSortPath();for(var vb=this.getChildren().length-1;vb>=0;vb--){var Ub=this.getChild(vb);if(Ub instanceof jsx3.gui.Column&&Ub.getSortPath()==mb)return this.getChild(vb).getDataType();}return jsx3.gui.Column.TYPETEXT;}else{return this.jsxsorttype;}};a.setSortType=function(k){this.jsxsorttype=k;return this;};a.getMaskProperties=function(){return jsx3.gui.Block.MASK_NO_EDIT;};a.getWrap=function(){return this.jsxwrap==null?jsx3.Boolean.TRUE:this.jsxwrap;};a.setWrap=function(n){this.resetXslCacheData();this.jsxwrap=n;return this;};a.onSetChild=function(p){return p instanceof jsx3.gui.Column;};a.g5=function(q){var Zb=this.getRecord(q);return Zb!=null&&(Zb[rb.ATTR_UNSELECTABLE]==null||Zb[rb.ATTR_UNSELECTABLE]!="1");};a.onDestroy=function(d){this.jsxsuper(d);this.onDestroyCached(d);this._jsxon=true;};h.getVersion=function(){return "3.0.00";};});jsx3.gui.List.prototype.getResizeable=jsx3.gui.List.prototype.getResizable;jsx3.gui.List.prototype.setResizeable=jsx3.gui.List.prototype.setResizable;jsx3.gui.List.prototype.getCanResize=jsx3.gui.List.prototype.getResizable;jsx3.gui.List.prototype.setCanResize=jsx3.gui.List.prototype.setResizable;jsx3.gui.List.prototype.doClearSelections=jsx3.gui.List.prototype.deselectAllRecords;jsx3.gui.List.prototype.deselectRecords=jsx3.gui.List.prototype.deselectAllRecords;jsx3.List=jsx3.gui.List;