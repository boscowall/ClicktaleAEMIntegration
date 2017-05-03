/*
 * Custom JavaScript to open a heatmap URL on a different browser window
 */
(function(document, $) {
	var ui = $(window).adaptTo("foundation-ui");
    $(document).on("click", ".open-heatmap", function(e) {
        var path = Granite.HTTP.externalize(window.location.pathname).replace(/\/[^/]*/, '');
        var json = null;
        $.ajax({
            'async': false,
            'global': false,
            'cache': false,
            'type': 'get',
            'url': '/bin/clicktale/properties',
            'data': {
                "path": path
            },
            'dataType': "json",
            'success': function(data) {
                json = data;
            }
        });
        
        if (isEmpty(json)){
        	//alert('Add Clicktale configuration to this site. Go to Sites->Select Site->View Properties->Cloud Services->Add Configuration->Clicktale Connector.');
        	ui.notify(Granite.I18n.get("Error"), "Add Clicktale Cloud Service to this Site: Go to Sites->Select Site->View Properties->Cloud Services->Add Configuration->Clicktale Connector.", "error");
        }
        else
        {
        	var url = "http://subs.app.clicktale.com/AggregatedReport.aspx?Type=OverlayReport&ReportType=MouseMoveHeatmap&UrlMode=Containing&GenerateReportNow=true&PID=";
        	url += json.pid;
        	url += "&Location=";
        	url += encodeURI(path);
			
        	window.open(url, '_blank');
        }
    });
    
    function isEmpty(obj) {
    for(var key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
    }
})(document, Granite.$);
