$(document).ready(function(){
		$("#controlers input").attr('disabled', true);
		$("#slider_seek").click(function(evt,arg){
			var left = evt.offsetX;
			DZ.player.seek((evt.offsetX/$(this).width()) * 100);
		});
	});

	function event_listener_append() {
		var pre = document.getElementById('event_listener');
		var line = [];
		for (var i = 0; i < arguments.length; i++) {
			line.push(arguments[i]);
		}
		pre.innerHTML += line.join(' ') + "\n";
	}
	
	function onPlayerLoaded() {
		$("#controlers input").attr('disabled', false);
		event_listener_append('player_loaded');
		DZ.Event.subscribe('current_track', function(arg){
			event_listener_append('current_track', arg.index, arg.track.title, arg.track.album.title);
		});
		DZ.Event.subscribe('player_position', function(arg){
			event_listener_append('position', arg[0], arg[1]);
			$("#slider_seek").find('.bar').css('width', (100*arg[0]/arg[1]) + '%');
		});
	}
	DZ.init(
	{
		appId  : '140663',
		channelUrl : 'http://localhost:8080/RecoMusic/index.xhtml',
		player : {
			container : 'player',
			cover : true,
			playlist : true,
			width : 650,
			height : 300,
			onload : onPlayerLoaded
		}
	});
	
	DZ.getLoginStatus(function(response)
	{
		if (response.authResponse)
		{
			// logged in and connected user, someone you know
		}
		else
		{
			// no user session available, someone you dont know
		}
	});
	
	DZ.login(function(response)
	{
		if (response.authResponse)
		{
			console.log('Welcome!  Fetching your information.... ');
			DZ.api('/user/me', function(response)
			{
				console.log('Good to see you, ' + response.name + '.');
			});
		} else {
			console.log('User cancelled login or did not fully authorize.');
		}
	}, {perms: 'basic_access,email'});
	
	
	function TocaMusica()
	{
		DZ.player.playTracks([68477709]);
	}