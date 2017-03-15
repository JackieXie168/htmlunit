/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.ChartComponent","jsx3.chart.PointRenderer");jsx3.Class.defineClass("jsx3.chart.Legend",jsx3.chart.ChartComponent,null,function(n,q){var L=jsx3.vector;var Sb=jsx3.chart;n.DEFAULT_WIDTH=100;n.DEFAULT_HEIGHT=100;n.aA=1;n.Nh=3;n.qj=2;n.SHOW_SERIES=1;n.SHOW_CATEGORIES=2;n.yn=8;n.hB=6;q.init=function(k){this.jsxsuper(k);this.boxHeight=10;this.lineHeight=22;this.labelClass=null;this.labelStyle=null;this.backgroundFill=null;this.backgroundStroke=null;this.preferredWidth=null;this.preferredHeight=null;this.setMargin("10 10 10 4");this.setPadding("4 4 0 4");};q.getBoxHeight=function(){return this.boxHeight;};q.setBoxHeight=function(m){this.boxHeight=m;};q.getLineHeight=function(){return this.lineHeight;};q.setLineHeight=function(b){this.lineHeight=b;};q.getLabelClass=function(){return this.labelClass;};q.setLabelClass=function(i){this.labelClass=i;};q.getLabelStyle=function(){return this.labelStyle;};q.setLabelStyle=function(j){this.labelStyle=j;};q.getBackgroundFill=function(){return this.backgroundFill;};q.setBackgroundFill=function(b){this.backgroundFill=b;};q.getBackgroundStroke=function(){return this.backgroundStroke;};q.setBackgroundStroke=function(r){this.backgroundStroke=r;};q.getPreferredWidth=function(){return this.preferredWidth!=null?this.preferredWidth:n.DEFAULT_WIDTH;};q.setPreferredWidth=function(d){this.preferredWidth=d;};q.getPreferredHeight=function(){return this.preferredHeight!=null?this.preferredHeight:n.DEFAULT_HEIGHT;};q.setPreferredHeight=function(b){this.preferredHeight=b;};q.updateView=function(){this.jsxsuper();var Jb=this.l5();this.A3();var sc=this.getChart();var x=sc.getLegendEntryType();var cc=0;if(x==n.SHOW_SERIES){cc=sc.aO().length;}else{if(x==n.SHOW_CATEGORIES){var Cc=sc.gH();if(Cc!=null)cc=Cc.length;}}var B=this.getLegendTitle();var eb=B!=null&&B.getDisplay()!=jsx3.gui.Block.DISPLAYNONE?B.getPreferredHeight()+n.yn:0;var Qb=this.getMarginDimensions();var Lc=this.getPaddingDimensions();var Mc=this.getWidth()-Qb[1]-Qb[3];var Ab=Math.min(this.getHeight()-Qb[0]-Qb[2],eb+this.lineHeight*cc+Lc[0]+Lc[2]);var oc=Qb[3];var qb=Math.max(Qb[0],Math.round((this.getHeight()-Ab)/2));var Bb=new L.Group(oc,qb,Mc,Ab);Jb.appendChild(Bb);Bb.setZIndex(n.qj);if(this.backgroundFill||this.backgroundStroke){if(cc>0||B!=null&&B.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){var ub=new L.Rectangle(oc,qb,Mc,Ab);ub.setZIndex(n.aA);Jb.appendChild(ub);var O=L.Fill.valueOf(this.backgroundFill);var R=L.Stroke.valueOf(this.backgroundStroke);ub.setFill(O);ub.setStroke(R);}}var z=qb+Lc[0];var mc=Mc-Lc[1]-Lc[3];if(B!=null&&B.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){B.setDimensions(oc+Lc[3],z,mc,B.getPreferredHeight());B.setZIndex(n.Nh);B.updateView();Jb.appendChild(B.l5());z=z+eb;}z=z-qb;var S=oc+Lc[3]+this.boxHeight+n.hB;var mb=mc-this.boxHeight-n.hB;if(x==n.SHOW_SERIES&&cc>0){var M=sc.aO();for(var wb=0;wb<M.length;wb++){var bb=M[wb].getLegendRenderer();var Rb=oc+Lc[3];var O=M[wb].D7();var R=M[wb].qT(O);var ab=bb.render(Rb,z,Rb+this.boxHeight,z+this.boxHeight,O,R);ab.setId(this.getId()+"_b"+wb);Bb.appendChild(ab);var uc=this.mD(Bb,M[wb].getSeriesName(),this.labelClass,this.labelStyle,S,Math.round(z+this.boxHeight/2),mb);z=z+this.lineHeight;this.A3(ab,M[wb],null);this.A3(uc,M[wb],null);}}else{if(x==n.SHOW_CATEGORIES&&cc>0){var Cc=sc.gH();var bb=Sb.PointRenderer.BOX;var R=L.Stroke.valueOf(sc.getSeriesStroke());for(var wb=0;wb<Cc.length;wb++){var Rb=oc+Lc[3];var O=sc.t0(Cc[wb],wb);var lb=R==null&&O.KJ()?new L.Stroke(O.getColor()):R;var ab=bb.render(Rb,z,Rb+this.boxHeight,z+this.boxHeight,O,lb);ab.setId(this.getId()+"_b"+wb);Bb.appendChild(ab);var Zb=sc.getCategoryField();var H=Zb?Cc[wb].getAttribute(Zb):"";var uc=this.mD(Bb,H,this.labelClass,this.labelStyle,S,Math.round(z+this.boxHeight/2),mb);z=z+this.lineHeight;this.A3(ab,null,wb);this.A3(uc,null,wb);}}}};q.mD=function(e,h,j,d,p,s,m){var Jc=new L.TextLine(p,s,m,s,h);Jc.setClassName(j);Jc.setExtraStyles(d);if(!Jc.getTextAlign())Jc.setTextAlign("left");e.appendChild(Jc);return Jc;};q.getLegendTitle=function(){return Sb.ChartLabel?this.getFirstChildOfType(Sb.ChartLabel):null;};q.onSetChild=function(c){return (Sb.ChartLabel&&c instanceof Sb.ChartLabel)&&this.getLegendTitle()==null;};q.onSetParent=function(p){return Sb.Chart&&p instanceof Sb.Chart;};q.onResize=function(){var Z=this.getParent();if(Z!=null){Z.repaint();}};q.toString=function(){return "[Legend '"+this.getName()+"']";};n.getVersion=function(){return Sb.q2;};q.A3=function(k,a,r){if(k==null)k=this.l5();if(a!=null)k.setProperty("seriesId",a.getId());if(r!=null)k.setProperty("recordIndex",r);this.jsxsuper(k);};q.IU=function(p,i){var y=i.getAttribute("seriesId");var Db=i.getAttribute("recordIndex");this.doEvent(jsx3.gui.Interactive.SELECT,this.DB(p,y,Db));};q.LH=function(r,p){var Kc=p.getAttribute("seriesId");var G=p.getAttribute("recordIndex");this.doEvent(jsx3.gui.Interactive.EXECUTE,this.DB(r,Kc,G));};q.doSpyOver=function(j,o){var Tb=o.getAttribute("seriesId");var P=o.getAttribute("recordIndex");this.jsxsupermix(j,o,this.DB(j,Tb,P));};q._4=function(k,b){var R=b.getAttribute("seriesId");var Kc=b.getAttribute("recordIndex");var vb;if(k.rightButton()&&(vb=this.getMenu())!=null){var bc=this.getServer().getJSXByName(vb);if(bc!=null){var kc=this.DB(k,R,Kc);kc.objMENU=bc;var Ib=this.doEvent(jsx3.gui.Interactive.MENU,kc);if(Ib!==false){if(Ib instanceof Object&&Ib.objMENU instanceof jsx3.gui.Menu)bc=Ib.objMENU;bc.showContextMenu(k,this);}}}};q.DB=function(c,b,f){var H={objEVENT:c};H.objSERIES=b!=null?this.getServer().getJSXById(b):null;if(f!=null){H.intINDEX=f;var cb=this.getChart().gH()[f];H.strRECORDID=cb?cb.getAttribute("jsxid"):null;}else{H.intINDEX=H.strRECORDID=null;}return H;};});
