if (self.CavalryLogger) { CavalryLogger.start_js(["++JNW"]); }

__d("ExternalEventSourceTypes",[],(function a(b,c,d,e,f,g){f.exports={APP:"APP",PIXEL:"PIXEL",OFFLINE_CONVERSION_DATA_SET:"OFFLINE_CONVERSION_DATA_SET",VIDEO:"VIDEO",PAGE:"PAGE",EVENT_SOURCE_GROUP:"EVENT_SOURCE_GROUP",EVENT:"EVENT",LEAD_GEN:"LEAD_GEN",IG_BUSINESS:"IG_BUSINESS",CANVAS:"CANVAS"}}),null);
__d("HovercardLinkParams",["HovercardLink"],(function a(b,c,d,e,f,g){f.exports={getHovercardParams:function h(i,j,k){return{"data-hovercard":c("HovercardLink").constructEndpointWithGroupLocationAndExtraParams({id:i},null,null,null,j).addQueryData(k).toString()}}}}),null);
__d("InfoTableRow.react",["cx","fbt","invariant","FbtResultBase","React","ReactFragment","Tooltip.react","joinClasses","monitorCodeUse"],(function a(b,c,d,e,f,g,h,i,j){var k,l,m=c("React").PropTypes;k=babelHelpers.inherits(n,c("React").Component);l=k&&k.prototype;n.prototype.render=function(){"use strict";var o=this.props.children;if(o!=null&&!Array.isArray(o))o=[o];if(o===null)o=[];var p=this.props.columns;o.length<p||j(0);var q=this.props.label;if(q&&this.props.useColon)q=i._("{label}:",[i.param("label",q)]);var r=this.props.annotation;if(r)r=c("React").createElement("div",{className:"_3stu fss"},i._("({annotation})",[i.param("annotation",r)]));var s=c("ReactFragment").create({label:q,annotation:r,help:this.props.helpLink});if(this.props.labelTooltip)s=c("React").createElement(c("Tooltip.react"),{tooltip:this.props.labelTooltip},s);if(this.props.labelFor){this.props.type==="data"||j(0);s=c("React").createElement("label",{htmlFor:this.props.labelFor},s)}var t=null;if(p===3)t=c("React").createElement("td",{className:"_480v"},o[1]);return c("React").createElement("tr",babelHelpers["extends"]({},this.props,{className:c("joinClasses")(this.props.className,this.props.type==="data"?"_3stt":""),label:null,type:null}),c("React").createElement("th",{className:"_3sts"+(!this.props.label?" noLabel":"")},s),c("React").createElement("td",{className:"_480u"},o[0]),t)};function n(){"use strict";k.apply(this,arguments)}n.propTypes={annotation:m.string,columns:m.oneOf([2,3]),helpLink:m.object,label:m.oneOfType([m.node,m.string]),labelFor:m.string,labelTooltip:m.string,type:m.oneOf(["data","text"]),useColon:m.bool};n.defaultProps={columns:2,type:"text",useColon:false};f.exports=n}),null);
__d("InfoTable.react",["cx","invariant","InfoTableRow.react","React","joinClasses"],(function a(b,c,d,e,f,g,h,i){var j,k,l=c("React").PropTypes;j=babelHelpers.inherits(m,c("React").Component);k=j&&j.prototype;m.prototype.render=function(){"use strict";var n=this.props.children,o=-1;c("React").Children.forEach(n,function(q,r){if(q!==null)o=r});var p=c("React").Children.map(n,function(q,r){if(q===null)return null;q.type!==c("InfoTableRow.react")||i(0);var s={columns:this.props.columns};if(this.props.sectionborders&&(r<o||this.props.showlastspacer))s.border=true;return c("React").cloneElement(q,s)},this);return c("React").createElement("table",babelHelpers["extends"]({},this.props,{className:c("joinClasses")(this.props.className,"_3stn"+(!this.props.sectionborders?" _3stp":"")),role:"presentation"}),p)};function m(){"use strict";j.apply(this,arguments)}m.propTypes={columns:l.oneOf([2,3]),sectionborders:l.bool,showlastspacer:l.bool};m.defaultProps={columns:2};f.exports=m}),null);
__d("InfoTableSeparator.react",["cx","React","joinClasses"],(function a(b,c,d,e,f,g,h){var i,j,k=c("React").PropTypes;i=babelHelpers.inherits(l,c("React").Component);j=i&&i.prototype;l.prototype.render=function(){"use strict";return c("React").createElement("tr",babelHelpers["extends"]({},this.props,{className:c("joinClasses")(this.props.className,"_3sto")}),c("React").createElement("td",{colSpan:this.props.columns},c("React").createElement("hr",null)))};function l(){"use strict";i.apply(this,arguments)}l.propTypes={columns:k.oneOf([2,3])};l.defaultProps={columns:2};f.exports=l}),null);
__d("InfoTableSection.react",["InfoTableSeparator.react","React"],(function a(b,c,d,e,f,g){var h,i,j=c("React").PropTypes;h=babelHelpers.inherits(k,c("React").Component);i=h&&h.prototype;k.prototype.render=function(){"use strict";var l=c("React").Children.map(this.props.children,function(n){if(n)return c("React").cloneElement(n,{columns:this.props.columns});return n},this),m=null;if(this.props.border)m=c("React").createElement(c("InfoTableSeparator.react"),{columns:this.props.columns});return c("React").createElement("tbody",this.props,l,m)};function k(){"use strict";h.apply(this,arguments)}k.propTypes={border:j.bool,columns:j.oneOf([2,3])};k.defaultProps={columns:2};f.exports=k}),null);
__d("CRMTooltip.react",["cx","React","TooltipMixin"],(function a(b,c,d,e,f,g,h){"use strict";var i=c("React").createClass({displayName:"CRMTooltip",mixins:[c("TooltipMixin")],render:function j(){var k=this.props.tooltip;return k?c("React").createElement("span",{className:"_x29","data-hover":"tooltip","aria-label":k}):c("React").createElement("span",{className:"hidden_elem"})}});f.exports=i}),null);
__d("mergeArrays",[],(function a(b,c,d,e,f,g){function h(i,j){for(var k=0;k<j.length;k++)if(i.indexOf(j[k])<0)i.push(j[k]);return i}f.exports=h}),null);
__d("FalcorRefChannels",[],(function a(b,c,d,e,f,g){f.exports=Object.freeze({ANALYTICS_EVENT_SOURCE_GROUP_CREATE:"analytics_event_source_group_create",ANALYTICS_SETTINGS:"analytics_settings",AYMT:"aymt",BUSINESS_MANAGER_EVENT_SOURCE_GROUP_CREATE:"business_manager_event_source_group_create",BUSINESS_MANAGER_VIEW_ANALYTICS_LINK:"business_manager_view_analytics_link",DEVSITE:"devsite",EMAIL:"email",INSIGHTS:"insights",PIXEL_MANAGER_VIEW_ANALYTICS_LINK:"pixel_manager_view_analytics_link",EVENTS_MANAGER_VIEW_ANALYTICS_LINK:"events_manager_view_analytics_link",FBA_MARKETING_SITE:"fba_marketing_site",ADS:"ads",HELP_CENTER:"helpcenter"})}),null);
__d("XAppAnalyticsMainController",["XController"],(function a(b,c,d,e,f,g){f.exports=c("XController").create("/analytics/{?obj_id}/",{obj_id:{type:"Int"},section:{type:"String"},subsection:{type:"String"},custom_event_name:{type:"String"},no_dedupe:{type:"Bool",defaultValue:false},insight_data:{type:"String"},since:{type:"Int"},until:{type:"Int"},dashboard_id:{type:"Int"},__aref_src:{type:"String"},__aref_id:{type:"String"},nav_source:{type:"String"}})}),null);
__d("XBusinessHomeController",["XController"],(function a(b,c,d,e,f,g){f.exports=c("XController").create("/",{personal:{type:"Exists",defaultValue:false},business_id:{type:"FBID"}})}),null);
__d("XDeveloperAppController",["XController"],(function a(b,c,d,e,f,g){f.exports=c("XController").create("/apps/{app_id}/{?page}/{?tab}/",{app_id:{type:"Int",required:true},page:{type:"Enum",enumType:1},tab:{type:"String"},placement_id:{type:"Int"},active_placements_offset:{type:"Int"},disabled_placements_offset:{type:"Int"},blocked_placements_offset:{type:"Int"},alert_id:{type:"Int"},ref:{type:"String"},id:{type:"Int"},app_locale:{type:"String"},a_n:{type:"String"}})}),null);