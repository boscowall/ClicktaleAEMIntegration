/*
 * Custom JavaScript to open a heatmap URL on a different browser window
 */
(function(document, $) {

    $(document).on("click", ".open-heatmap", function(e) {
        var path = Granite.HTTP.externalize(window.location.pathname).replace(/\/[^/]*/, '');
        var json = null;
        $.ajax({
            'async': false,
            'global': false,
            'cache': false,
            'type': 'get',
            'url': '/bin/sample/properties',
            'data': {
                "path": path
            },
            'dataType': "json",
            'success': function(data) {
                json = data;
            }
        });
        
        var url = "http://www.dummyurl.com?Type=OverlayReport&ReportType=ClickHeatmap&PID=";
        url += json.pid;
        url += "&IntegrationToken=";
        url += json.integrationToken;
        url += "Location=";
        url += path;

        alert(url);
        window.open(url, '_blank');
    });
})(document, Granite.$);
