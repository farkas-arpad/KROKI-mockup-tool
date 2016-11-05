$(document).ready(function() {
        $('#dataTables-example').DataTable({
			iDisplayLength: 75,
            "columnDefs": [ {
				"targets": 'no-sort',
				"orderable": false,
			}]
    });
});

jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});