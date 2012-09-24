/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Axis");jsx3.Class.defineClass("jsx3.chart.CategoryAxis",jsx3.chart.Axis,null,function(q,b){q.TICKS_ALIGNED="aligned";q.TICKS_BETWEEN="between";q.Tf=200;q.Di={aligned:1,between:1};b.init=function(s,h,m){this.jsxsuper(s,h,m);this.tickAlignment=q.TICKS_BETWEEN;this.categoryField=null;this.paddingLow=null;this.paddingHigh=null;this.xI("dK",0);this.xI("uI",0);};b.getTickAlignment=function(){return this.tickAlignment;};b.setTickAlignment=function(p){if(q.Di[p]){this.tickAlignment=p;}else{throw new jsx3.IllegalArgumentException("tickAlignment",p);}};b.getCategoryField=function(){return this.categoryField;};b.setCategoryField=function(n){this.categoryField=n;};b.getPaddingLow=function(){return this.paddingLow!=null?this.paddingLow:0;};b.setPaddingLow=function(a){this.paddingLow=a;};b.getPaddingHigh=function(){return this.paddingHigh!=null?this.paddingHigh:0;};b.setPaddingHigh=function(c){this.paddingHigh=c;};b.g6=function(){this.qX("Y6");var Kb=this.getChart();if(Kb==null){this.xI("dK",0);this.xI("uI",0);}else{var Cb=Kb.aX(this,true);var rb=Kb.gH();this.xI("uI",Cb.length);this.xI("dK",rb!=null?rb.length:0);}};b.RQ=function(){var wc=this.Q0("Y6");if(wc!=null)return wc;var wb=this.Q0("dK");wc=[];if(wb<1)return wc;var Ec=this.getPaddingLow();var _=this.getPaddingHigh();var ac=this.tickAlignment==q.TICKS_BETWEEN?wb+1:wb;var N=ac-1;var hc=N+Ec+_;var Jb=this.length/hc;var Fb=Ec*Jb;for(var gb=0;gb<ac&&gb<q.Tf;gb++){wc.push(Math.round(Fb+gb*Jb));}this.xI("Y6",wc);return wc;};b.w4=function(){var V=this.Q0("dK");if(this.tickAlignment==q.TICKS_BETWEEN){var cb=this.RQ();var sc=[];for(var E=0;E<V;E++){sc[E]=Math.round((cb[E]+cb[E+1])/2);}return sc;}else{return this.RQ();}};b.a1=function(d){var rb=d;var x=this.getChart();if(this.categoryField&&x!=null){var Z=x.gH();if(Z!=null){var Yb=Z[d];if(Yb!=null)rb=Yb.getAttribute([this.categoryField]);}}return rb;};b.vX=function(){return false;};b.getRangeForCategory=function(f){var Gb=this.RQ();if(this.tickAlignment==q.TICKS_BETWEEN){if(f<0||f>=Gb.length-1)return null;else return [Gb[f],Gb[f+1]];}else{if(f<0||f>=Gb.length||Gb.length<2)return null;var Dc=f==0?Gb[1]-Gb[0]:Gb[f]-Gb[f-1];return [Math.round(Gb[f]-Dc/2),Math.round(Gb[f]+Dc/2)];}};b.getPointForCategory=function(r){var F=this.RQ();if(this.tickAlignment==q.TICKS_BETWEEN){if(r<0||r>=F.length-1)return null;else return Math.round((F[r]+F[r+1])/2);}else{return F[r];}};b.toString=function(){return "[CategoryAxis '"+this.getName()+"' hor:"+this.horizontal+" pri:"+this.primary+"]";};q.getVersion=function(){return jsx3.chart.q2;};});
