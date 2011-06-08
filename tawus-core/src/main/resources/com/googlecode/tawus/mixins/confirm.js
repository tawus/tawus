ConfirmDialog = Class.create({
	initialize : function(spec) {
		Event.observe($(spec.id), spec.event, function(event) {
			var result = confirm(spec.message);
			if (result == false) {
				Event.stop(event);
				return false;
			}
		});
	}
});
