if (self.CavalryLogger) { CavalryLogger.start_js(["rK9jU"]); }

__d("DevsiteAppPlatformAndroidAppEventsCard",["cx","fbt","DevsiteAppPlatformCard","FalcorCustomEventsSettingsText","Grid.react","Link.react","React","SourceCode","XDeveloperDocumentationController","XUICardSection.react","XUIText.react","DevappSwitchButton.react","DevsiteAndroidSettingImplicitPurchaseWarningDialog","XDeveloperSaveAppEventsSettingsAsyncController","AsyncRequest","DeveloperAppPlatformSetupEnum"],(function a(b,c,d,e,f,g,h,i){"use strict";var j,k,l=c("Grid.react").GridItem,m=c("React").PropTypes,n={implicitPurchaseEvents:{header:c("FalcorCustomEventsSettingsText").AndroidImplicitPurchaseEvents.header,description:c("FalcorCustomEventsSettingsText").AndroidImplicitPurchaseEvents.description,notice:i._("{'Notice:' in Bold}{notice text}",[i.param("'Notice:' in Bold",c("React").createElement("b",null,"Notice: ")),i.param("notice text",c("FalcorCustomEventsSettingsText").AndroidImplicitPurchaseEvents.notice)]),failedToUpdateSettings:i._("Se ha producido un problema al actualizar la configuraci\u00f3n de los eventos de la aplicaci\u00f3n. Vuelve a intentarlo m\u00e1s tarde.")}};j=babelHelpers.inherits(o,c("React").Component);k=j&&j.prototype;function o(){var p,q;for(var r=arguments.length,s=Array(r),t=0;t<r;t++)s[t]=arguments[t];return q=(p=k.constructor).call.apply(p,[this].concat(s)),this.state={errorMessage:null,disableToggle:false,doesLogAndroidImplicitPurchaseEvents:this.props.doesLogAndroidImplicitPurchaseEvents},this.$DevsiteAppPlatformAndroidAppEventsCard1=function(){if(!this.state.doesLogAndroidImplicitPurchaseEvents)c("DevsiteAndroidSettingImplicitPurchaseWarningDialog").showSupportedSDKWarning();var u=c("XDeveloperSaveAppEventsSettingsAsyncController").getURIBuilder().setInt("app_id",this.props.appID).getURI(),v=!this.state.doesLogAndroidImplicitPurchaseEvents,w={log_android_implicit_purchase_events:v,platform:c("DeveloperAppPlatformSetupEnum").ANDROID};new(c("AsyncRequest"))().setURI(u).setMethod("POST").setData(w).setHandler(this.$DevsiteAppPlatformAndroidAppEventsCard2).setErrorHandler(this.$DevsiteAppPlatformAndroidAppEventsCard3).send();this.setState({errorMessage:null,disableToggle:true,doesLogAndroidImplicitPurchaseEvents:v})}.bind(this),this.$DevsiteAppPlatformAndroidAppEventsCard2=function(){this.setState({errorMessage:n.failedToUpdateSettings,disableToggle:false})}.bind(this),this.$DevsiteAppPlatformAndroidAppEventsCard3=function(u){this.setState({errorMessage:n.failedToUpdateSettings,disableToggle:false,doesLogAndroidImplicitPurchaseEvents:!this.state.doesLogAndroidImplicitPurchaseEvents})}.bind(this),q}o.prototype.render=function(){var p=null;if(this.props.showAndroidImplicitPurchaseEventsSwitch)p=c("React").createElement("div",null,c("React").createElement(c("XUIText.react"),{display:"block",className:"_4wrc _3-8q"},i._("Does your app support in-app purchases?")),c("React").createElement(c("Grid.react"),{cols:2,alignv:"middle",spacing:"_2pi8 _2pi5"},c("React").createElement(l,null,c("React").createElement(c("DevappSwitchButton.react"),{checked:this.state.doesLogAndroidImplicitPurchaseEvents,disabled:this.state.disableToggle,onChange:this.$DevsiteAppPlatformAndroidAppEventsCard1,size:"small"})),c("React").createElement(l,null,c("React").createElement("div",{className:"_3nil"},n.implicitPurchaseEvents.header),c("React").createElement("div",{className:"_3nim"},n.implicitPurchaseEvents.description),c("React").createElement("div",{className:"_3nim"},n.implicitPurchaseEvents.notice))));var q=c("XDeveloperDocumentationController").getURIBuilder().setString("path1","app-events").setString("path2","android").getURI(),r=c("React").createElement("div",null,c("React").createElement(c("XUIText.react"),{display:"block",className:"_gzy"},i._("App Events let you measure installs on your mobile app ads, create high value audiences for targeting, and view analytics including user demographics. Some events are logged after adding and configuring the Facebook SDK for your app. These events include install, activate, and deactivate events. For details see the {app_events_guide_link} .",[i.param("app_events_guide_link",c("React").createElement(c("Link.react"),{href:q,target:"_blank"},"App Events Guide"))])));return c("React").createElement(c("DevsiteAppPlatformCard"),null,c("React").createElement(c("XUICardSection.react"),null,c("React").createElement(c("XUIText.react"),{display:"block",className:"_4w65"},i._("Track App Installs and App Opens")),r,c("React").createElement(c("XUIText.react"),{display:"block",className:"_gzy"},i._("Now, when people install or engage with your app, you'll see this data reflected in your app's {=insights-dashboard-link}.",[i.param("=insights-dashboard-link",c("React").createElement("a",{target:"_blank",href:"https://www.facebook.com/insights/"+this.props.appID+"?section=AppEvents"},"Insights dashboard"))])),p))};o.propTypes={appID:m.string.isRequired,showAndroidImplicitPurchaseEventsSwitch:m.bool,doesLogAndroidImplicitPurchaseEvents:m.bool};f.exports=o}),null);