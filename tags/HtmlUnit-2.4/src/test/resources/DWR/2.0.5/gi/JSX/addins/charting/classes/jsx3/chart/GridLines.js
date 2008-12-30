/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.Class.defineClass("jsx3.chart.GridLines",jsx3.chart.ChartComponent,null,function(s,h){var U=jsx3.vector;var B=U.Stroke;h.init=function(l,p,j,d,a){this.jsxsuper(l);this.setDimensions(p,j,d,a);this.useXPrimary=jsx3.Boolean.TRUE;this.useYPrimary=jsx3.Boolean.TRUE;this.horizontalAbove=jsx3.Boolean.TRUE;this.inForeground=jsx3.Boolean.FALSE;this.borderStroke=null;this.fillV=null;this.strokeMajorV=null;this.strokeMinorV=null;this.fillH=null;this.strokeMajorH=null;this.strokeMinorH=null;};h.getUseXPrimary=function(){return this.useXPrimary;};h.setUseXPrimary=function(i){this.useXPrimary=i;};h.getUseYPrimary=function(){return this.useYPrimary;};h.setUseYPrimary=function(k){this.useYPrimary=k;};h.getHorizontalAbove=function(){return this.horizontalAbove;};h.setHorizontalAbove=function(n){this.horizontalAbove=n;};h.getInForeground=function(){return this.inForeground;};h.setInForeground=function(d){this.inForeground=d;};h.getBorderStroke=function(){return this.borderStroke;};h.setBorderStroke=function(m){this.borderStroke=m;};h.getFillV=function(){return this.fillV;};h.setFillV=function(i){this.fillV=i;};h.getStrokeMajorV=function(){return this.strokeMajorV;};h.setStrokeMajorV=function(o){this.strokeMajorV=o;};h.getStrokeMinorV=function(){return this.strokeMinorV;};h.setStrokeMinorV=function(a){this.strokeMinorV=a;};h.getFillH=function(){return this.fillH;};h.setFillH=function(p){this.fillH=p;};h.getStrokeMajorH=function(){return this.strokeMajorH;};h.setStrokeMajorH=function(b){this.strokeMajorH=b;};h.getStrokeMinorH=function(){return this.strokeMinorH;};h.setStrokeMinorH=function(p){this.strokeMinorH=p;};h.getXAxis=function(){var J=this.getChart();if(J==null)return null;return this.useXPrimary?J.getPrimaryXAxis():J.getSecondaryXAxis();};h.getYAxis=function(){var Bc=this.getChart();if(Bc==null)return null;return this.useYPrimary?Bc.getPrimaryYAxis():Bc.getSecondaryYAxis();};h.updateView=function(){this.jsxsuper();var Ub=this.l5();this.A3();var nc=new U.Group();nc.setZIndex(2);Ub.appendChild(nc);var cc=new U.Group();cc.setZIndex(this.horizontalAbove?3:1);Ub.appendChild(cc);var W=this.getWidth();var Ac=this.getHeight();if(this.borderStroke){var Zb=B.valueOf(this.borderStroke);var Mb=new U.Rectangle(0,0,W,Ac);Mb.setZIndex(10);Mb.setStroke(Zb);Ub.appendChild(Mb);}this.Xy(nc,W,Ac,this.getXAxis(),this.fillV,this.strokeMajorV,this.strokeMinorV,false);this.Xy(cc,W,Ac,this.getYAxis(),this.fillH,this.strokeMajorH,this.strokeMinorH,true);};h.Xy=function(j,l,p,c,g,r,e,n){j.setDimensions(0,0,l,p);var Ub=this.tV(g);if(c!=null){var Ib=c.RQ();if(Ib.length==0||Ub==1){this.ry(j,0,0,l,p,this.w6(g,0));}if(Ib.length>0){if(Ub>1)this.td(j,c,Ib,g,null,n);this.S7(j,c,Ib,r,null,n);this.Rk(j,c,Ib,e,n);}}else{this.ry(j,0,0,l,p,this.w6(g,0));}};h.td=function(e,r,q,b,m,o){if(m==null)m=new Array(this.tV(b));if(m.length==0)return;var P=e.getHeight();var S=e.getWidth();var Nb=o?P:S;var vc=o?S:P;for(var w=0;w<=q.length;w++){var Q=m[w%m.length];if(Q==null){Q=m[w%m.length]=new U.RectangleGroup(0,0,S,P);Q.setFill(this.w6(b,w));e.appendChild(Q);}if(w==q.length){if(q[w-1]<Nb)this.C6(Q,q[w-1],0,Nb,vc,o);}else{if(w==0){if(q[w]>0)this.C6(Q,0,0,q[w],vc,o);}else{this.C6(Q,q[w-1],0,q[w],vc,o);}}}};h.C6=function(f,p,d,o,c,l){if(l)f.C6(d,p,c,o);else f.C6(p,d,o,c);};h.S7=function(d,i,g,o,n,p){if(n==null)n=new Array(this.tV(o));if(n.length==0)return;var yb=d.getHeight();var Dc=d.getWidth();for(var N=0;N<g.length;N++){var Ob=n[N%n.length];if(Ob==null){Ob=n[N%n.length]=new U.LineGroup(0,0,Dc,yb);Ob.setStroke(this.GT(o,N));d.appendChild(Ob);}if(p)Ob.aT(0,g[N],Dc,0);else Ob.aT(g[N],0,0,yb);}};h.Rk=function(m,e,k,j,l){var Eb=this.tV(j);if(Eb==0)return;var S=new Array(Eb);for(var z=0;z<k.length;z++){var nc=e.RY(k,z);this.S7(m,e,nc,j,S,l);}};h.ry=function(j,d,o,l,p,k,i){if(k!=null){var qb=new U.Rectangle(d,o,l,p);if(i!=null)qb.setZIndex(i);qb.setFill(k);j.appendChild(qb);}};h.toString=function(){return "[GridLines '"+this.getName()+"']";};h.w6=function(q,f){if(q==null)return null;if(q instanceof Array){if(q.length>0){return U.Fill.valueOf(q[f%q.length]);}else{return null;}}else{return U.Fill.valueOf(q);}};h.GT=function(c,a){if(c==null)return null;if(c instanceof Array){if(c.length>0){return B.valueOf(c[a%c.length]);}else{return null;}}else{return B.valueOf(c);}};h.tV=function(o){if(o==null)return 0;return o instanceof Array?o.length:1;};h.onSetChild=function(){return false;};h.onSetParent=function(l){return jsx3.chart.Chart&&l instanceof jsx3.chart.Chart;};s.getVersion=function(){return jsx3.chart.q2;};});
