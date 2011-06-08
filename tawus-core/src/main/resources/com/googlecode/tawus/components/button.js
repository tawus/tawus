Tapestry.Initializer.ButtonEvent = function(spec) {
	this.element = $(spec.id);
	$T(this.element).zoneId = spec.zone;
	this.url = spec.url;
	Event.observe(this.element, spec.event, function(event) {
		if (!event.stopped) {
			if (spec.zone != null) {
				var zoneManager = Tapestry.findZoneManager($(spec.id));
				if (zoneManager) {
					zoneManager.updateFromURL(spec.url);
					$(spec.id).fire(Tapestry.TRIGGER_ZONE_UPDATE_EVENT);
					return;
				}
			}
			document.location = spec.url;
		}
	});
};
