/*
Copyright (c) 2007, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.net/yui/license.txt
version: 2.3.0
*/

YAHOO.widget.AutoComplete=function(elInput,elContainer,oDataSource,oConfigs){if(elInput&&elContainer&&oDataSource){if(oDataSource instanceof YAHOO.widget.DataSource){this.dataSource=oDataSource;}
else{return;}
if(YAHOO.util.Dom.inDocument(elInput)){if(YAHOO.lang.isString(elInput)){this._sName="instance"+YAHOO.widget.AutoComplete._nIndex+" "+elInput;this._oTextbox=document.getElementById(elInput);}
else{this._sName=(elInput.id)?"instance"+YAHOO.widget.AutoComplete._nIndex+" "+elInput.id:"instance"+YAHOO.widget.AutoComplete._nIndex;this._oTextbox=elInput;}
YAHOO.util.Dom.addClass(this._oTextbox,"yui-ac-input");}
else{return;}
if(YAHOO.util.Dom.inDocument(elContainer)){if(YAHOO.lang.isString(elContainer)){this._oContainer=document.getElementById(elContainer);}
else{this._oContainer=elContainer;}
if(this._oContainer.style.display=="none"){}
var elParent=this._oContainer.parentNode;var elTag=elParent.tagName.toLowerCase();while(elParent&&(elParent!="document")){if(elTag=="div"){YAHOO.util.Dom.addClass(elParent,"yui-ac");break;}
else{elParent=elParent.parentNode;elTag=elParent.tagName.toLowerCase();}}
if(elTag!="div"){}}
else{return;}
if(oConfigs&&(oConfigs.constructor==Object)){for(var sConfig in oConfigs){if(sConfig){this[sConfig]=oConfigs[sConfig];}}}
this._initContainer();this._initProps();this._initList();this._initContainerHelpers();var oSelf=this;var oTextbox=this._oTextbox;var oContent=this._oContainer._oContent;YAHOO.util.Event.addListener(oTextbox,"keyup",oSelf._onTextboxKeyUp,oSelf);YAHOO.util.Event.addListener(oTextbox,"keydown",oSelf._onTextboxKeyDown,oSelf);YAHOO.util.Event.addListener(oTextbox,"focus",oSelf._onTextboxFocus,oSelf);YAHOO.util.Event.addListener(oTextbox,"blur",oSelf._onTextboxBlur,oSelf);YAHOO.util.Event.addListener(oContent,"mouseover",oSelf._onContainerMouseover,oSelf);YAHOO.util.Event.addListener(oContent,"mouseout",oSelf._onContainerMouseout,oSelf);YAHOO.util.Event.addListener(oContent,"scroll",oSelf._onContainerScroll,oSelf);YAHOO.util.Event.addListener(oContent,"resize",oSelf._onContainerResize,oSelf);if(oTextbox.form){YAHOO.util.Event.addListener(oTextbox.form,"submit",oSelf._onFormSubmit,oSelf);}
YAHOO.util.Event.addListener(oTextbox,"keypress",oSelf._onTextboxKeyPress,oSelf);this.textboxFocusEvent=new YAHOO.util.CustomEvent("textboxFocus",this);this.textboxKeyEvent=new YAHOO.util.CustomEvent("textboxKey",this);this.dataRequestEvent=new YAHOO.util.CustomEvent("dataRequest",this);this.dataReturnEvent=new YAHOO.util.CustomEvent("dataReturn",this);this.dataErrorEvent=new YAHOO.util.CustomEvent("dataError",this);this.containerExpandEvent=new YAHOO.util.CustomEvent("containerExpand",this);this.typeAheadEvent=new YAHOO.util.CustomEvent("typeAhead",this);this.itemMouseOverEvent=new YAHOO.util.CustomEvent("itemMouseOver",this);this.itemMouseOutEvent=new YAHOO.util.CustomEvent("itemMouseOut",this);this.itemArrowToEvent=new YAHOO.util.CustomEvent("itemArrowTo",this);this.itemArrowFromEvent=new YAHOO.util.CustomEvent("itemArrowFrom",this);this.itemSelectEvent=new YAHOO.util.CustomEvent("itemSelect",this);this.unmatchedItemSelectEvent=new YAHOO.util.CustomEvent("unmatchedItemSelect",this);this.selectionEnforceEvent=new YAHOO.util.CustomEvent("selectionEnforce",this);this.containerCollapseEvent=new YAHOO.util.CustomEvent("containerCollapse",this);this.textboxBlurEvent=new YAHOO.util.CustomEvent("textboxBlur",this);oTextbox.setAttribute("autocomplete","off");YAHOO.widget.AutoComplete._nIndex++;}
else{}};YAHOO.widget.AutoComplete.prototype.dataSource=null;YAHOO.widget.AutoComplete.prototype.minQueryLength=1;YAHOO.widget.AutoComplete.prototype.maxResultsDisplayed=10;YAHOO.widget.AutoComplete.prototype.queryDelay=0.2;YAHOO.widget.AutoComplete.prototype.highlightClassName="yui-ac-highlight";YAHOO.widget.AutoComplete.prototype.prehighlightClassName=null;YAHOO.widget.AutoComplete.prototype.delimChar=null;YAHOO.widget.AutoComplete.prototype.autoHighlight=true;YAHOO.widget.AutoComplete.prototype.typeAhead=false;YAHOO.widget.AutoComplete.prototype.animHoriz=false;YAHOO.widget.AutoComplete.prototype.animVert=true;YAHOO.widget.AutoComplete.prototype.animSpeed=0.3;YAHOO.widget.AutoComplete.prototype.forceSelection=false;YAHOO.widget.AutoComplete.prototype.allowBrowserAutocomplete=true;YAHOO.widget.AutoComplete.prototype.alwaysShowContainer=false;YAHOO.widget.AutoComplete.prototype.useIFrame=false;YAHOO.widget.AutoComplete.prototype.useShadow=false;YAHOO.widget.AutoComplete.prototype.toString=function(){return"AutoComplete "+this._sName;};YAHOO.widget.AutoComplete.prototype.isContainerOpen=function(){return this._bContainerOpen;};YAHOO.widget.AutoComplete.prototype.getListItems=function(){return this._aListItems;};YAHOO.widget.AutoComplete.prototype.getListItemData=function(oListItem){if(oListItem._oResultData){return oListItem._oResultData;}
else{return false;}};YAHOO.widget.AutoComplete.prototype.setHeader=function(sHeader){if(sHeader){if(this._oContainer._oContent._oHeader){this._oContainer._oContent._oHeader.innerHTML=sHeader;this._oContainer._oContent._oHeader.style.display="block";}}
else{this._oContainer._oContent._oHeader.innerHTML="";this._oContainer._oContent._oHeader.style.display="none";}};YAHOO.widget.AutoComplete.prototype.setFooter=function(sFooter){if(sFooter){if(this._oContainer._oContent._oFooter){this._oContainer._oContent._oFooter.innerHTML=sFooter;this._oContainer._oContent._oFooter.style.display="block";}}
else{this._oContainer._oContent._oFooter.innerHTML="";this._oContainer._oContent._oFooter.style.display="none";}};YAHOO.widget.AutoComplete.prototype.setBody=function(sBody){if(sBody){if(this._oContainer._oContent._oBody){this._oContainer._oContent._oBody.innerHTML=sBody;this._oContainer._oContent._oBody.style.display="block";this._oContainer._oContent.style.display="block";}}
else{this._oContainer._oContent._oBody.innerHTML="";this._oContainer._oContent.style.display="none";}
this._maxResultsDisplayed=0;};YAHOO.widget.AutoComplete.prototype.formatResult=function(oResultItem,sQuery){var sResult=oResultItem[0];if(sResult){return sResult;}
else{return"";}};YAHOO.widget.AutoComplete.prototype.doBeforeExpandContainer=function(oTextbox,oContainer,sQuery,aResults){return true;};YAHOO.widget.AutoComplete.prototype.sendQuery=function(sQuery){this._sendQuery(sQuery);};YAHOO.widget.AutoComplete.prototype.doBeforeSendQuery=function(sQuery){return sQuery;};YAHOO.widget.AutoComplete.prototype.destroy=function(){var instanceName=this.toString();var elInput=this._oTextbox;var elContainer=this._oContainer;this.textboxFocusEvent.unsubscribe();this.textboxKeyEvent.unsubscribe();this.dataRequestEvent.unsubscribe();this.dataReturnEvent.unsubscribe();this.dataErrorEvent.unsubscribe();this.containerExpandEvent.unsubscribe();this.typeAheadEvent.unsubscribe();this.itemMouseOverEvent.unsubscribe();this.itemMouseOutEvent.unsubscribe();this.itemArrowToEvent.unsubscribe();this.itemArrowFromEvent.unsubscribe();this.itemSelectEvent.unsubscribe();this.unmatchedItemSelectEvent.unsubscribe();this.selectionEnforceEvent.unsubscribe();this.containerCollapseEvent.unsubscribe();this.textboxBlurEvent.unsubscribe();YAHOO.util.Event.purgeElement(elInput,true);YAHOO.util.Event.purgeElement(elContainer,true);elContainer.innerHTML="";for(var key in this){if(this.hasOwnProperty(key)){this[key]=null;}}};YAHOO.widget.AutoComplete.prototype.textboxFocusEvent=null;YAHOO.widget.AutoComplete.prototype.textboxKeyEvent=null;YAHOO.widget.AutoComplete.prototype.dataRequestEvent=null;YAHOO.widget.AutoComplete.prototype.dataReturnEvent=null;YAHOO.widget.AutoComplete.prototype.dataErrorEvent=null;YAHOO.widget.AutoComplete.prototype.containerExpandEvent=null;YAHOO.widget.AutoComplete.prototype.typeAheadEvent=null;YAHOO.widget.AutoComplete.prototype.itemMouseOverEvent=null;YAHOO.widget.AutoComplete.prototype.itemMouseOutEvent=null;YAHOO.widget.AutoComplete.prototype.itemArrowToEvent=null;YAHOO.widget.AutoComplete.prototype.itemArrowFromEvent=null;YAHOO.widget.AutoComplete.prototype.itemSelectEvent=null;YAHOO.widget.AutoComplete.prototype.unmatchedItemSelectEvent=null;YAHOO.widget.AutoComplete.prototype.selectionEnforceEvent=null;YAHOO.widget.AutoComplete.prototype.containerCollapseEvent=null;YAHOO.widget.AutoComplete.prototype.textboxBlurEvent=null;YAHOO.widget.AutoComplete._nIndex=0;YAHOO.widget.AutoComplete.prototype._sName=null;YAHOO.widget.AutoComplete.prototype._oTextbox=null;YAHOO.widget.AutoComplete.prototype._bFocused=true;YAHOO.widget.AutoComplete.prototype._oAnim=null;YAHOO.widget.AutoComplete.prototype._oContainer=null;YAHOO.widget.AutoComplete.prototype._bContainerOpen=false;YAHOO.widget.AutoComplete.prototype._bOverContainer=false;YAHOO.widget.AutoComplete.prototype._aListItems=null;YAHOO.widget.AutoComplete.prototype._nDisplayedItems=0;YAHOO.widget.AutoComplete.prototype._maxResultsDisplayed=0;YAHOO.widget.AutoComplete.prototype._sCurQuery=null;YAHOO.widget.AutoComplete.prototype._sSavedQuery=null;YAHOO.widget.AutoComplete.prototype._oCurItem=null;YAHOO.widget.AutoComplete.prototype._bItemSelected=false;YAHOO.widget.AutoComplete.prototype._nKeyCode=null;YAHOO.widget.AutoComplete.prototype._nDelayID=-1;YAHOO.widget.AutoComplete.prototype._iFrameSrc="javascript:false;";YAHOO.widget.AutoComplete.prototype._queryInterval=null;YAHOO.widget.AutoComplete.prototype._sLastTextboxValue=null;YAHOO.widget.AutoComplete.prototype._initProps=function(){var minQueryLength=this.minQueryLength;if(!YAHOO.lang.isNumber(minQueryLength)){this.minQueryLength=1;}
var maxResultsDisplayed=this.maxResultsDisplayed;if(!YAHOO.lang.isNumber(maxResultsDisplayed)||(maxResultsDisplayed<1)){this.maxResultsDisplayed=10;}
var queryDelay=this.queryDelay;if(!YAHOO.lang.isNumber(queryDelay)||(queryDelay<0)){this.queryDelay=0.2;}
var delimChar=this.delimChar;if(YAHOO.lang.isString(delimChar)){this.delimChar=[delimChar];}
else if(!YAHOO.lang.isArray(delimChar)){this.delimChar=null;}
var animSpeed=this.animSpeed;if((this.animHoriz||this.animVert)&&YAHOO.util.Anim){if(!YAHOO.lang.isNumber(animSpeed)||(animSpeed<0)){this.animSpeed=0.3;}
if(!this._oAnim){this._oAnim=new YAHOO.util.Anim(this._oContainer._oContent,{},this.animSpeed);}
else{this._oAnim.duration=this.animSpeed;}}
if(this.forceSelection&&delimChar){}};YAHOO.widget.AutoComplete.prototype._initContainerHelpers=function(){if(this.useShadow&&!this._oContainer._oShadow){var oShadow=document.createElement("div");oShadow.className="yui-ac-shadow";this._oContainer._oShadow=this._oContainer.appendChild(oShadow);}
if(this.useIFrame&&!this._oContainer._oIFrame){var oIFrame=document.createElement("iframe");oIFrame.src=this._iFrameSrc;oIFrame.frameBorder=0;oIFrame.scrolling="no";oIFrame.style.position="absolute";oIFrame.style.width="100%";oIFrame.style.height="100%";oIFrame.tabIndex=-1;this._oContainer._oIFrame=this._oContainer.appendChild(oIFrame);}};YAHOO.widget.AutoComplete.prototype._initContainer=function(){YAHOO.util.Dom.addClass(this._oContainer,"yui-ac-container");if(!this._oContainer._oContent){var oContent=document.createElement("div");oContent.className="yui-ac-content";oContent.style.display="none";this._oContainer._oContent=this._oContainer.appendChild(oContent);var oHeader=document.createElement("div");oHeader.className="yui-ac-hd";oHeader.style.display="none";this._oContainer._oContent._oHeader=this._oContainer._oContent.appendChild(oHeader);var oBody=document.createElement("div");oBody.className="yui-ac-bd";this._oContainer._oContent._oBody=this._oContainer._oContent.appendChild(oBody);var oFooter=document.createElement("div");oFooter.className="yui-ac-ft";oFooter.style.display="none";this._oContainer._oContent._oFooter=this._oContainer._oContent.appendChild(oFooter);}
else{}};YAHOO.widget.AutoComplete.prototype._initList=function(){this._aListItems=[];while(this._oContainer._oContent._oBody.hasChildNodes()){var oldListItems=this.getListItems();if(oldListItems){for(var oldi=oldListItems.length-1;oldi>=0;oldi--){oldListItems[oldi]=null;}}
this._oContainer._oContent._oBody.innerHTML="";}
var oList=document.createElement("ul");oList=this._oContainer._oContent._oBody.appendChild(oList);for(var i=0;i<this.maxResultsDisplayed;i++){var oItem=document.createElement("li");oItem=oList.appendChild(oItem);this._aListItems[i]=oItem;this._initListItem(oItem,i);}
this._maxResultsDisplayed=this.maxResultsDisplayed;};YAHOO.widget.AutoComplete.prototype._initListItem=function(oItem,nItemIndex){var oSelf=this;oItem.style.display="none";oItem._nItemIndex=nItemIndex;oItem.mouseover=oItem.mouseout=oItem.onclick=null;YAHOO.util.Event.addListener(oItem,"mouseover",oSelf._onItemMouseover,oSelf);YAHOO.util.Event.addListener(oItem,"mouseout",oSelf._onItemMouseout,oSelf);YAHOO.util.Event.addListener(oItem,"click",oSelf._onItemMouseclick,oSelf);};YAHOO.widget.AutoComplete.prototype._onIMEDetected=function(oSelf){oSelf._enableIntervalDetection();};YAHOO.widget.AutoComplete.prototype._enableIntervalDetection=function(){var currValue=this._oTextbox.value;var lastValue=this._sLastTextboxValue;if(currValue!=lastValue){this._sLastTextboxValue=currValue;this._sendQuery(currValue);}};YAHOO.widget.AutoComplete.prototype._cancelIntervalDetection=function(oSelf){if(oSelf._queryInterval){clearInterval(oSelf._queryInterval);}};YAHOO.widget.AutoComplete.prototype._isIgnoreKey=function(nKeyCode){if((nKeyCode==9)||(nKeyCode==13)||(nKeyCode==16)||(nKeyCode==17)||(nKeyCode>=18&&nKeyCode<=20)||(nKeyCode==27)||(nKeyCode>=33&&nKeyCode<=35)||(nKeyCode>=36&&nKeyCode<=40)||(nKeyCode>=44&&nKeyCode<=45)){return true;}
return false;};YAHOO.widget.AutoComplete.prototype._sendQuery=function(sQuery){if(this.minQueryLength==-1){this._toggleContainer(false);return;}
var aDelimChar=(this.delimChar)?this.delimChar:null;if(aDelimChar){var nDelimIndex=-1;for(var i=aDelimChar.length-1;i>=0;i--){var nNewIndex=sQuery.lastIndexOf(aDelimChar[i]);if(nNewIndex>nDelimIndex){nDelimIndex=nNewIndex;}}
if(aDelimChar[i]==" "){for(var j=aDelimChar.length-1;j>=0;j--){if(sQuery[nDelimIndex-1]==aDelimChar[j]){nDelimIndex--;break;}}}
if(nDelimIndex>-1){var nQueryStart=nDelimIndex+1;while(sQuery.charAt(nQueryStart)==" "){nQueryStart+=1;}
this._sSavedQuery=sQuery.substring(0,nQueryStart);sQuery=sQuery.substr(nQueryStart);}
else if(sQuery.indexOf(this._sSavedQuery)<0){this._sSavedQuery=null;}}
if((sQuery&&(sQuery.length<this.minQueryLength))||(!sQuery&&this.minQueryLength>0)){if(this._nDelayID!=-1){clearTimeout(this._nDelayID);}
this._toggleContainer(false);return;}
sQuery=encodeURIComponent(sQuery);this._nDelayID=-1;sQuery=this.doBeforeSendQuery(sQuery);this.dataRequestEvent.fire(this,sQuery);this.dataSource.getResults(this._populateList,sQuery,this);};YAHOO.widget.AutoComplete.prototype._populateList=function(sQuery,aResults,oSelf){if(aResults===null){oSelf.dataErrorEvent.fire(oSelf,sQuery);}
if(!oSelf._bFocused||!aResults){return;}
var isOpera=(navigator.userAgent.toLowerCase().indexOf("opera")!=-1);var contentStyle=oSelf._oContainer._oContent.style;contentStyle.width=(!isOpera)?null:"";contentStyle.height=(!isOpera)?null:"";var sCurQuery=decodeURIComponent(sQuery);oSelf._sCurQuery=sCurQuery;oSelf._bItemSelected=false;if(oSelf._maxResultsDisplayed!=oSelf.maxResultsDisplayed){oSelf._initList();}
var nItems=Math.min(aResults.length,oSelf.maxResultsDisplayed);oSelf._nDisplayedItems=nItems;if(nItems>0){oSelf._initContainerHelpers();var aItems=oSelf._aListItems;for(var i=nItems-1;i>=0;i--){var oItemi=aItems[i];var oResultItemi=aResults[i];oItemi.innerHTML=oSelf.formatResult(oResultItemi,sCurQuery);oItemi.style.display="list-item";oItemi._sResultKey=oResultItemi[0];oItemi._oResultData=oResultItemi;}
for(var j=aItems.length-1;j>=nItems;j--){var oItemj=aItems[j];oItemj.innerHTML=null;oItemj.style.display="none";oItemj._sResultKey=null;oItemj._oResultData=null;}
var ok=oSelf.doBeforeExpandContainer(oSelf._oTextbox,oSelf._oContainer,sQuery,aResults);oSelf._toggleContainer(ok);if(oSelf.autoHighlight){var oFirstItem=aItems[0];oSelf._toggleHighlight(oFirstItem,"to");oSelf.itemArrowToEvent.fire(oSelf,oFirstItem);oSelf._typeAhead(oFirstItem,sQuery);}
else{oSelf._oCurItem=null;}}
else{oSelf._toggleContainer(false);}
oSelf.dataReturnEvent.fire(oSelf,sQuery,aResults);};YAHOO.widget.AutoComplete.prototype._clearSelection=function(){var sValue=this._oTextbox.value;var sChar=(this.delimChar)?this.delimChar[0]:null;var nIndex=(sChar)?sValue.lastIndexOf(sChar,sValue.length-2):-1;if(nIndex>-1){this._oTextbox.value=sValue.substring(0,nIndex);}
else{this._oTextbox.value="";}
this._sSavedQuery=this._oTextbox.value;this.selectionEnforceEvent.fire(this);};YAHOO.widget.AutoComplete.prototype._textMatchesOption=function(){var foundMatch=null;for(var i=this._nDisplayedItems-1;i>=0;i--){var oItem=this._aListItems[i];var sMatch=oItem._sResultKey.toLowerCase();if(sMatch==this._sCurQuery.toLowerCase()){foundMatch=oItem;break;}}
return(foundMatch);};YAHOO.widget.AutoComplete.prototype._typeAhead=function(oItem,sQuery){if(!this.typeAhead||(this._nKeyCode==8)){return;}
var oTextbox=this._oTextbox;var sValue=this._oTextbox.value;if(!oTextbox.setSelectionRange&&!oTextbox.createTextRange){return;}
var nStart=sValue.length;this._updateValue(oItem);var nEnd=oTextbox.value.length;this._selectText(oTextbox,nStart,nEnd);var sPrefill=oTextbox.value.substr(nStart,nEnd);this.typeAheadEvent.fire(this,sQuery,sPrefill);};YAHOO.widget.AutoComplete.prototype._selectText=function(oTextbox,nStart,nEnd){if(oTextbox.setSelectionRange){oTextbox.setSelectionRange(nStart,nEnd);}
else if(oTextbox.createTextRange){var oTextRange=oTextbox.createTextRange();oTextRange.moveStart("character",nStart);oTextRange.moveEnd("character",nEnd-oTextbox.value.length);oTextRange.select();}
else{oTextbox.select();}};YAHOO.widget.AutoComplete.prototype._toggleContainerHelpers=function(bShow){var bFireEvent=false;var width=this._oContainer._oContent.offsetWidth+"px";var height=this._oContainer._oContent.offsetHeight+"px";if(this.useIFrame&&this._oContainer._oIFrame){bFireEvent=true;if(bShow){this._oContainer._oIFrame.style.width=width;this._oContainer._oIFrame.style.height=height;}
else{this._oContainer._oIFrame.style.width=0;this._oContainer._oIFrame.style.height=0;}}
if(this.useShadow&&this._oContainer._oShadow){bFireEvent=true;if(bShow){this._oContainer._oShadow.style.width=width;this._oContainer._oShadow.style.height=height;}
else{this._oContainer._oShadow.style.width=0;this._oContainer._oShadow.style.height=0;}}};YAHOO.widget.AutoComplete.prototype._toggleContainer=function(bShow){var oContainer=this._oContainer;if(this.alwaysShowContainer&&this._bContainerOpen){return;}
if(!bShow){this._oContainer._oContent.scrollTop=0;var aItems=this._aListItems;if(aItems&&(aItems.length>0)){for(var i=aItems.length-1;i>=0;i--){aItems[i].style.display="none";}}
if(this._oCurItem){this._toggleHighlight(this._oCurItem,"from");}
this._oCurItem=null;this._nDisplayedItems=0;this._sCurQuery=null;}
if(!bShow&&!this._bContainerOpen){oContainer._oContent.style.display="none";return;}
var oAnim=this._oAnim;if(oAnim&&oAnim.getEl()&&(this.animHoriz||this.animVert)){if(!bShow){this._toggleContainerHelpers(bShow);}
if(oAnim.isAnimated()){oAnim.stop();}
var oClone=oContainer._oContent.cloneNode(true);oContainer.appendChild(oClone);oClone.style.top="-9000px";oClone.style.display="block";var wExp=oClone.offsetWidth;var hExp=oClone.offsetHeight;var wColl=(this.animHoriz)?0:wExp;var hColl=(this.animVert)?0:hExp;oAnim.attributes=(bShow)?{width:{to:wExp},height:{to:hExp}}:{width:{to:wColl},height:{to:hColl}};if(bShow&&!this._bContainerOpen){oContainer._oContent.style.width=wColl+"px";oContainer._oContent.style.height=hColl+"px";}
else{oContainer._oContent.style.width=wExp+"px";oContainer._oContent.style.height=hExp+"px";}
oContainer.removeChild(oClone);oClone=null;var oSelf=this;var onAnimComplete=function(){oAnim.onComplete.unsubscribeAll();if(bShow){oSelf.containerExpandEvent.fire(oSelf);}
else{oContainer._oContent.style.display="none";oSelf.containerCollapseEvent.fire(oSelf);}
oSelf._toggleContainerHelpers(bShow);};oContainer._oContent.style.display="block";oAnim.onComplete.subscribe(onAnimComplete);oAnim.animate();this._bContainerOpen=bShow;}
else{if(bShow){oContainer._oContent.style.display="block";this.containerExpandEvent.fire(this);}
else{oContainer._oContent.style.display="none";this.containerCollapseEvent.fire(this);}
this._toggleContainerHelpers(bShow);this._bContainerOpen=bShow;}};YAHOO.widget.AutoComplete.prototype._toggleHighlight=function(oNewItem,sType){var sHighlight=this.highlightClassName;if(this._oCurItem){YAHOO.util.Dom.removeClass(this._oCurItem,sHighlight);}
if((sType=="to")&&sHighlight){YAHOO.util.Dom.addClass(oNewItem,sHighlight);this._oCurItem=oNewItem;}};YAHOO.widget.AutoComplete.prototype._togglePrehighlight=function(oNewItem,sType){if(oNewItem==this._oCurItem){return;}
var sPrehighlight=this.prehighlightClassName;if((sType=="mouseover")&&sPrehighlight){YAHOO.util.Dom.addClass(oNewItem,sPrehighlight);}
else{YAHOO.util.Dom.removeClass(oNewItem,sPrehighlight);}};YAHOO.widget.AutoComplete.prototype._updateValue=function(oItem){var oTextbox=this._oTextbox;var sDelimChar=(this.delimChar)?(this.delimChar[0]||this.delimChar):null;var sSavedQuery=this._sSavedQuery;var sResultKey=oItem._sResultKey;oTextbox.focus();oTextbox.value="";if(sDelimChar){if(sSavedQuery){oTextbox.value=sSavedQuery;}
oTextbox.value+=sResultKey+sDelimChar;if(sDelimChar!=" "){oTextbox.value+=" ";}}
else{oTextbox.value=sResultKey;}
if(oTextbox.type=="textarea"){oTextbox.scrollTop=oTextbox.scrollHeight;}
var end=oTextbox.value.length;this._selectText(oTextbox,end,end);this._oCurItem=oItem;};YAHOO.widget.AutoComplete.prototype._selectItem=function(oItem){this._bItemSelected=true;this._updateValue(oItem);this._cancelIntervalDetection(this);this.itemSelectEvent.fire(this,oItem,oItem._oResultData);this._toggleContainer(false);};YAHOO.widget.AutoComplete.prototype._jumpSelection=function(){if(this._oCurItem){this._selectItem(this._oCurItem);}
else{this._toggleContainer(false);}};YAHOO.widget.AutoComplete.prototype._moveSelection=function(nKeyCode){if(this._bContainerOpen){var oCurItem=this._oCurItem;var nCurItemIndex=-1;if(oCurItem){nCurItemIndex=oCurItem._nItemIndex;}
var nNewItemIndex=(nKeyCode==40)?(nCurItemIndex+1):(nCurItemIndex-1);if(nNewItemIndex<-2||nNewItemIndex>=this._nDisplayedItems){return;}
if(oCurItem){this._toggleHighlight(oCurItem,"from");this.itemArrowFromEvent.fire(this,oCurItem);}
if(nNewItemIndex==-1){if(this.delimChar&&this._sSavedQuery){if(!this._textMatchesOption()){this._oTextbox.value=this._sSavedQuery;}
else{this._oTextbox.value=this._sSavedQuery+this._sCurQuery;}}
else{this._oTextbox.value=this._sCurQuery;}
this._oCurItem=null;return;}
if(nNewItemIndex==-2){this._toggleContainer(false);return;}
var oNewItem=this._aListItems[nNewItemIndex];var oContent=this._oContainer._oContent;var scrollOn=((YAHOO.util.Dom.getStyle(oContent,"overflow")=="auto")||(YAHOO.util.Dom.getStyle(oContent,"overflowY")=="auto"));if(scrollOn&&(nNewItemIndex>-1)&&(nNewItemIndex<this._nDisplayedItems)){if(nKeyCode==40){if((oNewItem.offsetTop+oNewItem.offsetHeight)>(oContent.scrollTop+oContent.offsetHeight)){oContent.scrollTop=(oNewItem.offsetTop+oNewItem.offsetHeight)-oContent.offsetHeight;}
else if((oNewItem.offsetTop+oNewItem.offsetHeight)<oContent.scrollTop){oContent.scrollTop=oNewItem.offsetTop;}}
else{if(oNewItem.offsetTop<oContent.scrollTop){this._oContainer._oContent.scrollTop=oNewItem.offsetTop;}
else if(oNewItem.offsetTop>(oContent.scrollTop+oContent.offsetHeight)){this._oContainer._oContent.scrollTop=(oNewItem.offsetTop+oNewItem.offsetHeight)-oContent.offsetHeight;}}}
this._toggleHighlight(oNewItem,"to");this.itemArrowToEvent.fire(this,oNewItem);if(this.typeAhead){this._updateValue(oNewItem);}}};YAHOO.widget.AutoComplete.prototype._onItemMouseover=function(v,oSelf){if(oSelf.prehighlightClassName){oSelf._togglePrehighlight(this,"mouseover");}
else{oSelf._toggleHighlight(this,"to");}
oSelf.itemMouseOverEvent.fire(oSelf,this);};YAHOO.widget.AutoComplete.prototype._onItemMouseout=function(v,oSelf){if(oSelf.prehighlightClassName){oSelf._togglePrehighlight(this,"mouseout");}
else{oSelf._toggleHighlight(this,"from");}
oSelf.itemMouseOutEvent.fire(oSelf,this);};YAHOO.widget.AutoComplete.prototype._onItemMouseclick=function(v,oSelf){oSelf._toggleHighlight(this,"to");oSelf._selectItem(this);};YAHOO.widget.AutoComplete.prototype._onContainerMouseover=function(v,oSelf){oSelf._bOverContainer=true;};YAHOO.widget.AutoComplete.prototype._onContainerMouseout=function(v,oSelf){oSelf._bOverContainer=false;if(oSelf._oCurItem){oSelf._toggleHighlight(oSelf._oCurItem,"to");}};YAHOO.widget.AutoComplete.prototype._onContainerScroll=function(v,oSelf){oSelf._oTextbox.focus();};YAHOO.widget.AutoComplete.prototype._onContainerResize=function(v,oSelf){oSelf._toggleContainerHelpers(oSelf._bContainerOpen);};YAHOO.widget.AutoComplete.prototype._onTextboxKeyDown=function(v,oSelf){var nKeyCode=v.keyCode;switch(nKeyCode){case 9:if(oSelf._oCurItem){if(oSelf.delimChar&&(oSelf._nKeyCode!=nKeyCode)){if(oSelf._bContainerOpen){YAHOO.util.Event.stopEvent(v);}}
oSelf._selectItem(oSelf._oCurItem);}
else{oSelf._toggleContainer(false);}
break;case 13:if(oSelf._oCurItem){if(oSelf._nKeyCode!=nKeyCode){if(oSelf._bContainerOpen){YAHOO.util.Event.stopEvent(v);}}
oSelf._selectItem(oSelf._oCurItem);}
else{oSelf._toggleContainer(false);}
break;case 27:oSelf._toggleContainer(false);return;case 39:oSelf._jumpSelection();break;case 38:YAHOO.util.Event.stopEvent(v);oSelf._moveSelection(nKeyCode);break;case 40:YAHOO.util.Event.stopEvent(v);oSelf._moveSelection(nKeyCode);break;default:break;}};YAHOO.widget.AutoComplete.prototype._onTextboxKeyPress=function(v,oSelf){var nKeyCode=v.keyCode;var isMac=(navigator.userAgent.toLowerCase().indexOf("mac")!=-1);if(isMac){switch(nKeyCode){case 9:if(oSelf.delimChar&&(oSelf._nKeyCode!=nKeyCode)){YAHOO.util.Event.stopEvent(v);}
break;case 13:if(oSelf._nKeyCode!=nKeyCode){YAHOO.util.Event.stopEvent(v);}
break;case 38:case 40:YAHOO.util.Event.stopEvent(v);break;default:break;}}
else if(nKeyCode==229){oSelf._queryInterval=setInterval(function(){oSelf._onIMEDetected(oSelf);},500);}};YAHOO.widget.AutoComplete.prototype._onTextboxKeyUp=function(v,oSelf){oSelf._initProps();var nKeyCode=v.keyCode;oSelf._nKeyCode=nKeyCode;var sText=this.value;if(oSelf._isIgnoreKey(nKeyCode)||(sText.toLowerCase()==oSelf._sCurQuery)){return;}
else{oSelf._bItemSelected=false;YAHOO.util.Dom.removeClass(oSelf._oCurItem,oSelf.highlightClassName);oSelf._oCurItem=null;oSelf.textboxKeyEvent.fire(oSelf,nKeyCode);}
if(oSelf.queryDelay>0){var nDelayID=setTimeout(function(){oSelf._sendQuery(sText);},(oSelf.queryDelay*1000));if(oSelf._nDelayID!=-1){clearTimeout(oSelf._nDelayID);}
oSelf._nDelayID=nDelayID;}
else{oSelf._sendQuery(sText);}};YAHOO.widget.AutoComplete.prototype._onTextboxFocus=function(v,oSelf){oSelf._oTextbox.setAttribute("autocomplete","off");oSelf._bFocused=true;if(!oSelf._bItemSelected){oSelf.textboxFocusEvent.fire(oSelf);}};YAHOO.widget.AutoComplete.prototype._onTextboxBlur=function(v,oSelf){if(!oSelf._bOverContainer||(oSelf._nKeyCode==9)){if(!oSelf._bItemSelected){var oMatch=oSelf._textMatchesOption();if(!oSelf._bContainerOpen||(oSelf._bContainerOpen&&(oMatch===null))){if(oSelf.forceSelection){oSelf._clearSelection();}
else{oSelf.unmatchedItemSelectEvent.fire(oSelf,oSelf._sCurQuery);}}
else{oSelf._selectItem(oMatch);}}
if(oSelf._bContainerOpen){oSelf._toggleContainer(false);}
oSelf._cancelIntervalDetection(oSelf);oSelf._bFocused=false;oSelf.textboxBlurEvent.fire(oSelf);}};YAHOO.widget.AutoComplete.prototype._onFormSubmit=function(v,oSelf){if(oSelf.allowBrowserAutocomplete){oSelf._oTextbox.setAttribute("autocomplete","on");}
else{oSelf._oTextbox.setAttribute("autocomplete","off");}};YAHOO.widget.DataSource=function(){};YAHOO.widget.DataSource.ERROR_DATANULL="Response data was null";YAHOO.widget.DataSource.ERROR_DATAPARSE="Response data could not be parsed";YAHOO.widget.DataSource.prototype.maxCacheEntries=15;YAHOO.widget.DataSource.prototype.queryMatchContains=false;YAHOO.widget.DataSource.prototype.queryMatchSubset=false;YAHOO.widget.DataSource.prototype.queryMatchCase=false;YAHOO.widget.DataSource.prototype.toString=function(){return"DataSource "+this._sName;};YAHOO.widget.DataSource.prototype.getResults=function(oCallbackFn,sQuery,oParent){var aResults=this._doQueryCache(oCallbackFn,sQuery,oParent);if(aResults.length===0){this.queryEvent.fire(this,oParent,sQuery);this.doQuery(oCallbackFn,sQuery,oParent);}};YAHOO.widget.DataSource.prototype.doQuery=function(oCallbackFn,sQuery,oParent){};YAHOO.widget.DataSource.prototype.flushCache=function(){if(this._aCache){this._aCache=[];}
if(this._aCacheHelper){this._aCacheHelper=[];}
this.cacheFlushEvent.fire(this);};YAHOO.widget.DataSource.prototype.queryEvent=null;YAHOO.widget.DataSource.prototype.cacheQueryEvent=null;YAHOO.widget.DataSource.prototype.getResultsEvent=null;YAHOO.widget.DataSource.prototype.getCachedResultsEvent=null;YAHOO.widget.DataSource.prototype.dataErrorEvent=null;YAHOO.widget.DataSource.prototype.cacheFlushEvent=null;YAHOO.widget.DataSource._nIndex=0;YAHOO.widget.DataSource.prototype._sName=null;YAHOO.widget.DataSource.prototype._aCache=null;YAHOO.widget.DataSource.prototype._init=function(){var maxCacheEntries=this.maxCacheEntries;if(!YAHOO.lang.isNumber(maxCacheEntries)||(maxCacheEntries<0)){maxCacheEntries=0;}
if(maxCacheEntries>0&&!this._aCache){this._aCache=[];}
this._sName="instance"+YAHOO.widget.DataSource._nIndex;YAHOO.widget.DataSource._nIndex++;this.queryEvent=new YAHOO.util.CustomEvent("query",this);this.cacheQueryEvent=new YAHOO.util.CustomEvent("cacheQuery",this);this.getResultsEvent=new YAHOO.util.CustomEvent("getResults",this);this.getCachedResultsEvent=new YAHOO.util.CustomEvent("getCachedResults",this);this.dataErrorEvent=new YAHOO.util.CustomEvent("dataError",this);this.cacheFlushEvent=new YAHOO.util.CustomEvent("cacheFlush",this);};YAHOO.widget.DataSource.prototype._addCacheElem=function(oResult){var aCache=this._aCache;if(!aCache||!oResult||!oResult.query||!oResult.results){return;}
if(aCache.length>=this.maxCacheEntries){aCache.shift();}
aCache.push(oResult);};YAHOO.widget.DataSource.prototype._doQueryCache=function(oCallbackFn,sQuery,oParent){var aResults=[];var bMatchFound=false;var aCache=this._aCache;var nCacheLength=(aCache)?aCache.length:0;var bMatchContains=this.queryMatchContains;if((this.maxCacheEntries>0)&&aCache&&(nCacheLength>0)){this.cacheQueryEvent.fire(this,oParent,sQuery);if(!this.queryMatchCase){var sOrigQuery=sQuery;sQuery=sQuery.toLowerCase();}
for(var i=nCacheLength-1;i>=0;i--){var resultObj=aCache[i];var aAllResultItems=resultObj.results;var matchKey=(!this.queryMatchCase)?encodeURIComponent(resultObj.query).toLowerCase():encodeURIComponent(resultObj.query);if(matchKey==sQuery){bMatchFound=true;aResults=aAllResultItems;if(i!=nCacheLength-1){aCache.splice(i,1);this._addCacheElem(resultObj);}
break;}
else if(this.queryMatchSubset){for(var j=sQuery.length-1;j>=0;j--){var subQuery=sQuery.substr(0,j);if(matchKey==subQuery){bMatchFound=true;for(var k=aAllResultItems.length-1;k>=0;k--){var aRecord=aAllResultItems[k];var sKeyIndex=(this.queryMatchCase)?encodeURIComponent(aRecord[0]).indexOf(sQuery):encodeURIComponent(aRecord[0]).toLowerCase().indexOf(sQuery);if((!bMatchContains&&(sKeyIndex===0))||(bMatchContains&&(sKeyIndex>-1))){aResults.unshift(aRecord);}}
resultObj={};resultObj.query=sQuery;resultObj.results=aResults;this._addCacheElem(resultObj);break;}}
if(bMatchFound){break;}}}
if(bMatchFound){this.getCachedResultsEvent.fire(this,oParent,sOrigQuery,aResults);oCallbackFn(sOrigQuery,aResults,oParent);}}
return aResults;};YAHOO.widget.DS_XHR=function(sScriptURI,aSchema,oConfigs){if(oConfigs&&(oConfigs.constructor==Object)){for(var sConfig in oConfigs){this[sConfig]=oConfigs[sConfig];}}
if(!YAHOO.lang.isArray(aSchema)||!YAHOO.lang.isString(sScriptURI)){return;}
this.schema=aSchema;this.scriptURI=sScriptURI;this._init();};YAHOO.widget.DS_XHR.prototype=new YAHOO.widget.DataSource();YAHOO.widget.DS_XHR.TYPE_JSON=0;YAHOO.widget.DS_XHR.TYPE_XML=1;YAHOO.widget.DS_XHR.TYPE_FLAT=2;YAHOO.widget.DS_XHR.ERROR_DATAXHR="XHR response failed";YAHOO.widget.DS_XHR.prototype.connMgr=YAHOO.util.Connect;YAHOO.widget.DS_XHR.prototype.connTimeout=0;YAHOO.widget.DS_XHR.prototype.scriptURI=null;YAHOO.widget.DS_XHR.prototype.scriptQueryParam="query";YAHOO.widget.DS_XHR.prototype.scriptQueryAppend="";YAHOO.widget.DS_XHR.prototype.responseType=YAHOO.widget.DS_XHR.TYPE_JSON;YAHOO.widget.DS_XHR.prototype.responseStripAfter="\n<!-";YAHOO.widget.DS_XHR.prototype.doQuery=function(oCallbackFn,sQuery,oParent){var isXML=(this.responseType==YAHOO.widget.DS_XHR.TYPE_XML);var sUri=this.scriptURI+"?"+this.scriptQueryParam+"="+sQuery;if(this.scriptQueryAppend.length>0){sUri+="&"+this.scriptQueryAppend;}
var oResponse=null;var oSelf=this;var responseSuccess=function(oResp){if(!oSelf._oConn||(oResp.tId!=oSelf._oConn.tId)){oSelf.dataErrorEvent.fire(oSelf,oParent,sQuery,YAHOO.widget.DataSource.ERROR_DATANULL);return;}
for(var foo in oResp){}
if(!isXML){oResp=oResp.responseText;}
else{oResp=oResp.responseXML;}
if(oResp===null){oSelf.dataErrorEvent.fire(oSelf,oParent,sQuery,YAHOO.widget.DataSource.ERROR_DATANULL);return;}
var aResults=oSelf.parseResponse(sQuery,oResp,oParent);var resultObj={};resultObj.query=decodeURIComponent(sQuery);resultObj.results=aResults;if(aResults===null){oSelf.dataErrorEvent.fire(oSelf,oParent,sQuery,YAHOO.widget.DataSource.ERROR_DATAPARSE);aResults=[];}
else{oSelf.getResultsEvent.fire(oSelf,oParent,sQuery,aResults);oSelf._addCacheElem(resultObj);}
oCallbackFn(sQuery,aResults,oParent);};var responseFailure=function(oResp){oSelf.dataErrorEvent.fire(oSelf,oParent,sQuery,YAHOO.widget.DS_XHR.ERROR_DATAXHR);return;};var oCallback={success:responseSuccess,failure:responseFailure};if(YAHOO.lang.isNumber(this.connTimeout)&&(this.connTimeout>0)){oCallback.timeout=this.connTimeout;}
if(this._oConn){this.connMgr.abort(this._oConn);}
oSelf._oConn=this.connMgr.asyncRequest("GET",sUri,oCallback,null);};YAHOO.widget.DS_XHR.prototype.parseResponse=function(sQuery,oResponse,oParent){var aSchema=this.schema;var aResults=[];var bError=false;var nEnd=((this.responseStripAfter!=="")&&(oResponse.indexOf))?oResponse.indexOf(this.responseStripAfter):-1;if(nEnd!=-1){oResponse=oResponse.substring(0,nEnd);}
switch(this.responseType){case YAHOO.widget.DS_XHR.TYPE_JSON:var jsonList,jsonObjParsed;var isNotMac=(navigator.userAgent.toLowerCase().indexOf('khtml')==-1);if(oResponse.parseJSON&&isNotMac){jsonObjParsed=oResponse.parseJSON();if(!jsonObjParsed){bError=true;}
else{try{jsonList=eval("jsonObjParsed."+aSchema[0]);}
catch(e){bError=true;break;}}}
else if(window.JSON&&isNotMac){jsonObjParsed=JSON.parse(oResponse);if(!jsonObjParsed){bError=true;break;}
else{try{jsonList=eval("jsonObjParsed."+aSchema[0]);}
catch(e){bError=true;break;}}}
else{try{while(oResponse.substring(0,1)==" "){oResponse=oResponse.substring(1,oResponse.length);}
if(oResponse.indexOf("{")<0){bError=true;break;}
if(oResponse.indexOf("{}")===0){break;}
var jsonObjRaw=eval("("+oResponse+")");if(!jsonObjRaw){bError=true;break;}
jsonList=eval("(jsonObjRaw."+aSchema[0]+")");}
catch(e){bError=true;break;}}
if(!jsonList){bError=true;break;}
if(!YAHOO.lang.isArray(jsonList)){jsonList=[jsonList];}
for(var i=jsonList.length-1;i>=0;i--){var aResultItem=[];var jsonResult=jsonList[i];for(var j=aSchema.length-1;j>=1;j--){var dataFieldValue=jsonResult[aSchema[j]];if(!dataFieldValue){dataFieldValue="";}
aResultItem.unshift(dataFieldValue);}
if(aResultItem.length==1){aResultItem.push(jsonResult);}
aResults.unshift(aResultItem);}
break;case YAHOO.widget.DS_XHR.TYPE_XML:var xmlList=oResponse.getElementsByTagName(aSchema[0]);if(!xmlList){bError=true;break;}
for(var k=xmlList.length-1;k>=0;k--){var result=xmlList.item(k);var aFieldSet=[];for(var m=aSchema.length-1;m>=1;m--){var sValue=null;var xmlAttr=result.attributes.getNamedItem(aSchema[m]);if(xmlAttr){sValue=xmlAttr.value;}
else{var xmlNode=result.getElementsByTagName(aSchema[m]);if(xmlNode&&xmlNode.item(0)&&xmlNode.item(0).firstChild){sValue=xmlNode.item(0).firstChild.nodeValue;}
else{sValue="";}}
aFieldSet.unshift(sValue);}
aResults.unshift(aFieldSet);}
break;case YAHOO.widget.DS_XHR.TYPE_FLAT:if(oResponse.length>0){var newLength=oResponse.length-aSchema[0].length;if(oResponse.substr(newLength)==aSchema[0]){oResponse=oResponse.substr(0,newLength);}
var aRecords=oResponse.split(aSchema[0]);for(var n=aRecords.length-1;n>=0;n--){aResults[n]=aRecords[n].split(aSchema[1]);}}
break;default:break;}
sQuery=null;oResponse=null;oParent=null;if(bError){return null;}
else{return aResults;}};YAHOO.widget.DS_XHR.prototype._oConn=null;YAHOO.widget.DS_JSFunction=function(oFunction,oConfigs){if(oConfigs&&(oConfigs.constructor==Object)){for(var sConfig in oConfigs){this[sConfig]=oConfigs[sConfig];}}
if(!YAHOO.lang.isFunction(oFunction)){return;}
else{this.dataFunction=oFunction;this._init();}};YAHOO.widget.DS_JSFunction.prototype=new YAHOO.widget.DataSource();YAHOO.widget.DS_JSFunction.prototype.dataFunction=null;YAHOO.widget.DS_JSFunction.prototype.doQuery=function(oCallbackFn,sQuery,oParent){var oFunction=this.dataFunction;var aResults=[];aResults=oFunction(sQuery);if(aResults===null){this.dataErrorEvent.fire(this,oParent,sQuery,YAHOO.widget.DataSource.ERROR_DATANULL);return;}
var resultObj={};resultObj.query=decodeURIComponent(sQuery);resultObj.results=aResults;this._addCacheElem(resultObj);this.getResultsEvent.fire(this,oParent,sQuery,aResults);oCallbackFn(sQuery,aResults,oParent);return;};YAHOO.widget.DS_JSArray=function(aData,oConfigs){if(oConfigs&&(oConfigs.constructor==Object)){for(var sConfig in oConfigs){this[sConfig]=oConfigs[sConfig];}}
if(!YAHOO.lang.isArray(aData)){return;}
else{this.data=aData;this._init();}};YAHOO.widget.DS_JSArray.prototype=new YAHOO.widget.DataSource();YAHOO.widget.DS_JSArray.prototype.data=null;YAHOO.widget.DS_JSArray.prototype.doQuery=function(oCallbackFn,sQuery,oParent){var i;var aData=this.data;var aResults=[];var bMatchFound=false;var bMatchContains=this.queryMatchContains;if(sQuery){if(!this.queryMatchCase){sQuery=sQuery.toLowerCase();}
for(i=aData.length-1;i>=0;i--){var aDataset=[];if(YAHOO.lang.isString(aData[i])){aDataset[0]=aData[i];}
else if(YAHOO.lang.isArray(aData[i])){aDataset=aData[i];}
if(YAHOO.lang.isString(aDataset[0])){var sKeyIndex=(this.queryMatchCase)?encodeURIComponent(aDataset[0]).indexOf(sQuery):encodeURIComponent(aDataset[0]).toLowerCase().indexOf(sQuery);if((!bMatchContains&&(sKeyIndex===0))||(bMatchContains&&(sKeyIndex>-1))){aResults.unshift(aDataset);}}}}
else{for(i=aData.length-1;i>=0;i--){if(YAHOO.lang.isString(aData[i])){aResults.unshift([aData[i]]);}
else if(YAHOO.lang.isArray(aData[i])){aResults.unshift(aData[i]);}}}
this.getResultsEvent.fire(this,oParent,sQuery,aResults);oCallbackFn(sQuery,aResults,oParent);};YAHOO.register("autocomplete",YAHOO.widget.AutoComplete,{version:"2.3.0",build:"442"});