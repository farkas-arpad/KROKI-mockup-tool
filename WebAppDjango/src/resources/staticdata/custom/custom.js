var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

$(document).ready(function() {  
var rowId = getUrlParameter('id'); 
if(typeof rowId != "undefined")
{
  $('#selectable-table tbody tr:nth-child('+rowId+')').addClass('active').siblings().removeClass('active');
}
});

$(document).ready(function() {
        $('#dataTables-example').DataTable({
                responsive: true
        });
    });

jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});