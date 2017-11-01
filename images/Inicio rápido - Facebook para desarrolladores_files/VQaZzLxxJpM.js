if (self.CavalryLogger) { CavalryLogger.start_js(["l0xxw"]); }

__d("DeveloperAppPlatformSetup",["React","ReactDOM","Arbiter","Animation","DevsiteAppPlatformFinishedCard","DevsiteQuickStartLogger","Event","DeveloperAppPlatformSlidePresenter","Vector"],(function a(b,c,d,e,f,g){var h={CONDITIONAL_PATH:[],_indexUpdateScrollThreshold:[],_scrollSelectedIndex:0,_ArbiterToken:null,_getStepInstances:function i(){return this.state.steps.map(function(j,k){return j.map(function(l,m){return this.refs["slide_"+k+"_"+m]}.bind(this))}.bind(this))},_getSlideInstance:function i(j){if(j>this.state.maxSlide||j<0)return null;for(var k=0;k<this.state.steps.length;k++){if(j<this.state.steps[k].length)return this.refs["slide_"+k+"_"+j];j-=this.state.steps[k].length}return null},componentDidMount:function i(){var j=this._getStepInstances();this._initializeIndexUpdateScrollThreshold(j);c("DeveloperAppPlatformSlidePresenter").init(j);this._scrollListener=c("Event").listen(window,"scroll",this._handleScroll);this._handleScroll();this._ArbiterToken=c("Arbiter").subscribe("devsiteAppPlatformProgressBar/clickUpdate",function(k,l){if(l.selectedIndex>this.state.maxSlide){this.setState({slide:this.state.maxSlide});c("Arbiter").inform("devsiteAppPlatformProgressBar/blink",{index:this.state.maxSlide})}else this.setState({slide:l.selectedIndex})}.bind(this))},componentWillUnmount:function i(){c("Arbiter").unsubscribe(this._ArbiterToken);this._scrollListener.remove()},componentDidUpdate:function i(){var j=this._getStepInstances();c("DeveloperAppPlatformSlidePresenter").update(j);this.updateSteps();c("DeveloperAppPlatformSlidePresenter").scrollToSlide(this._getSlideInstance(this.state.slide))},getInitialState:function i(){var j=this.getSteps();j[j.length-1].push(c("React").createElement(c("DevsiteAppPlatformFinishedCard"),null));return{slide:0,step:0,steps:j,maxSlide:j[0].length-1}},setSlidesForStep:function i(j,k){var l=this.state.steps;if(j===l.length-1)k.push(c("React").createElement(c("DevsiteAppPlatformFinishedCard"),null));l[j]=k;this.updateSteps()},updateSteps:function i(){this._initializeIndexUpdateScrollThreshold(this._getStepInstances())},_defaultAsyncSuccessHandler:function i(j,k){k.payload.success&&setTimeout(j,0)},_next:function i(j,k){var l=this.state.steps;if(j>=l.length-1)return;var m=l[j+1].length+k;this.setState({step:++j,slide:++k,maxSlide:m});this.props.onChange&&this.props.onChange(j,k)},_isSetupComplete:function i(){return this.state.step==this.state.steps.length-1},_hasRelatedProgressbarStepName:function i(j,k){var l=this.state.steps.length-1;return j!==l||k<this.state.steps[l].length-1},isSlideVisible:function i(j){return j<=this.state.maxSlide},_sendArbiterRequest:function i(){c("Arbiter").inform("devsiteAppPlatformProgressBar/update",{selectedIndex:this._scrollSelectedIndex})},_handleScroll:function i(){var j=this._scrollSelectedIndex;this._updateSelectedIndex();if(j!==this._scrollSelectedIndex){this._sendArbiterRequest();var k=this._indexUpdateScrollThreshold.length;if(j==k-1||this._scrollSelectedIndex==k-1){var l=this.state.steps.length-1,m=this.state.steps[l].length-1,n=c("ReactDOM").findDOMNode(this.refs["slide_"+l+"_"+m]),o=this._scrollSelectedIndex==k-1;new(c("Animation"))(n).from("opacity",o?0:1).to("opacity",o?1:0).duration(400).go();if(!this._loggedTourEnded){this._loggedTourEnded=true;c("DevsiteQuickStartLogger").log("tourEnded")}}}},_initializeIndexUpdateScrollThreshold:function i(j){this._indexUpdateScrollThreshold=[];j.forEach(function(k,l){return k.forEach(function(m,n){return this._hasRelatedProgressbarStepName(l,n)&&this._indexUpdateScrollThreshold.push(c("Vector").getElementPosition(c("ReactDOM").findDOMNode(m)).y-c("DeveloperAppPlatformSlidePresenter").CARD_BROWSWER_SCROLL_MARGIN-5)}.bind(this))}.bind(this))},_tourEnded:function i(){var j=c("Vector").getDocumentDimensions().y===c("Vector").getScrollPosition().y+c("Vector").getViewportDimensions().y,k=this.isSlideVisible(this._indexUpdateScrollThreshold.length);return j&&k},_updateSelectedIndex:function i(){var j=c("Vector").getScrollPosition().y;this._scrollSelectedIndex=0;this._indexUpdateScrollThreshold.forEach(function(k,l){if(j>k&&this.isSlideVisible(l))this._scrollSelectedIndex=l}.bind(this));if(this._tourEnded())this._scrollSelectedIndex=this._indexUpdateScrollThreshold.length-1},render:function i(){var j=[],k=0;this.state.steps.forEach(function(l,m){l.forEach(function(n,o){var p=n.props.forwardAction||this._next.bind(this,m,k),q=c("React").cloneElement(n,{visible:this.isSlideVisible(k),forwardAction:p,asyncSuccessHandler:n.props.asyncSuccessHandler||this._defaultAsyncSuccessHandler.bind(this,p),key:"slide_"+m+"_"+o,index:k,isFinishIcon:m==this.state.steps.length-1&&o==this.state.steps[m].length-1,ref:"slide_"+m+"_"+o});j.push(q);k++}.bind(this))}.bind(this));return c("React").createElement("div",null,j)}};f.exports=h}),null);