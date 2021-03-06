var DEEZER_DEMO_API_KEY='9QB1EM63CLM2RR5V3';

var audio;
jQuery.ajaxSettings.traditional = true;

function createSongDiv(song) 
{
	var songDiv = $("<div class='sdiv' id=" + song.id + "-div> ");

	return songDiv;
}

function mostrarCurtir(nome)
{
	document.getElementById('hiddenForm:IHIdMusica').value = nome;
	document.getElementById('hiddenForm:cbPesquisaCurtiu').click(); 
	if(document.getElementById('hiddenForm:IHCurtiuMusica').value=='true' && document.getElementById('hiddenForm:IHNaoCurtiuMusica').value=='false')
	{
		return "<div class=\"form-group\"><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"naoCurtir('" + nome + "');\" class=\"btn-default\"> <img id=\"btCurtirDescurtir" + nome + "\" src=\"https://cdn1.iconfinder.com/data/icons/business-votes/512/unlike_2-16.png\"/></a><a href=\"#\" style=\"padding:5px 20px;\"/><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"curtir('" + nome + "');\" class=\"btn-default\"> <img id=\"btCurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/linecons-free-vector-icons-pack/32/like-16.png\"/></a></div>";
	}
	else if(document.getElementById('hiddenForm:IHCurtiuMusica').value=='false' && document.getElementById('hiddenForm:IHNaoCurtiuMusica').value=='true')
	{
		return "<div class=\"form-group\"><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"naoCurtir('" + nome + "');\" class=\"btn-default\"> <img id=\"btCurtirDescurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/stroke/53/Unlike-16.png\"/></a><a href=\"#\" style=\"padding:5px 20px;\"/><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"curtir('" + nome + "');\" class=\"btn-default\"> <img id=\"btCurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/wpzoom-developer-icon-set/500/138-16.png\"/></a></div>";
	}
	else if(document.getElementById('hiddenForm:IHCurtiuMusica').value=='false' && document.getElementById('hiddenForm:IHNaoCurtiuMusica').value=='false')
	{
		return "<div class=\"form-group\"><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"naoCurtir('" + nome + "');\" class=\"btn-default\"> <img id=\"btCurtirDescurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/stroke/53/Unlike-16.png\"/></a><a href=\"#\" style=\"padding:5px 20px;\"/><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"curtir('" + nome + "');\" class=\"btn-default\"> <img id=\"btCurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/linecons-free-vector-icons-pack/32/like-16.png\"/></a></div>";
	}
}

function mostrarCompartilhar(IDMUsica)
{
	return "<a id=\"sharebutton\" href=\"#\" onclick=\"compartilharFacebook('" + IDMUsica + "');\">Compartilhar no Facebook</a>";
}

function compartilharFacebook(ID) {
	
	var url = 'http://api.deezer.com/2.0/track/' + ID + '?callback=?';
	jQuery.getJSON(url, { output:'jsonp'},
			
			function(data) {
		
		FB.ui({
			method: 'feed',
			name: 'RecMusic',
			link: 'http://localhost:8080/RecoMusic/',
			picture: data.album.cover,
			caption: data.title + ' - ' + data.artist.name,
			description: data.album.title 
		});
	});
	
}

function curtir(nome)
{
	document.getElementById('hiddenForm:IHIdMusica').value = nome;
	document.getElementById('hiddenForm:cbAvaliaMusicaPositivamente').click(); 
	
	if(document.getElementById('hiddenForm:IHCurtiuMusica').value=='true')
	{
		document.getElementById('btCurtir' + nome).src = "https://cdn3.iconfinder.com/data/icons/wpzoom-developer-icon-set/500/138-16.png";
		document.getElementById('btCurtirDescurtir' + nome).src = "https://cdn3.iconfinder.com/data/icons/stroke/53/Unlike-16.png";
	}
	else 
	{
		document.getElementById('btCurtir' + nome).src = "https://cdn3.iconfinder.com/data/icons/linecons-free-vector-icons-pack/32/like-16.png";
	}
}

function naoCurtir(nome)
{

	document.getElementById('hiddenForm:IHIdMusica').value = nome;
	document.getElementById('hiddenForm:cbAvaliaMusicaNegativamente').click(); 

	if(document.getElementById('hiddenForm:IHNaoCurtiuMusica').value=='true')
	{
		document.getElementById('btCurtir' + nome).src = "https://cdn3.iconfinder.com/data/icons/linecons-free-vector-icons-pack/32/like-16.png";
		document.getElementById('btCurtirDescurtir' + nome).src = "https://cdn1.iconfinder.com/data/icons/business-votes/512/unlike_2-16.png";
	}
	else
	{
		document.getElementById('btCurtirDescurtir' + nome).src = "https://cdn3.iconfinder.com/data/icons/stroke/53/Unlike-16.png";
	}
}

function addSongs(songs) {
	var playlist = $("#playlist");
	for (var i = 0; i < songs.length; i++) 
	{
		var div = createSongDiv(songs[i]);
		playlist.append(div);
		fetchDeezerTrack(songs[i], div);
	}
}

//http://developer.echonest.com/api/v4/artist/profile?api_key=9QB1EM63CLM2RR5V3&id=ARH3S5S1187FB4F76B&bucket=genre&format=json
function fetchDeezerTrack(song, div) {
	if (song.tracks.length > 0) {
		var tid = song.tracks[0].foreign_id.split(':')[2];
		var url = 'http://api.deezer.com/2.0/track/' + tid + '?callback=?';
		jQuery.getJSON(url, { output:'jsonp'},
				function(data) {
			var link = $("<a target='deezer'>").attr('href', data.link);
			var cover = $("<img class='timg'>").attr('src', data.album.cover).attr("style", "float:left");
			link.append(cover);
			div.append(link);

			var tdiv = $("<div class='tdiv'>");
			tdiv.append( $("<div class='title'>").text(data.title));
			tdiv.append( $("<div>").text(data.album.title));
			tdiv.append( $("<div>").text(data.artist.name));
			div.append(tdiv);
			div.append(createPlayer(data.preview));
			div.append($('<br clear="left">'));
			div.append($(mostrarCurtir(song.id)));
			div.append($(mostrarCompartilhar(tid)));
		}
		);
	}
}

function createPlayButton(audio) {
	var button = $("<button>").text("preview");
	button.click( 
			function()  {
				alert("Playing " + audio);
			}
	);
	return button;
}


function createPlayer(audio) {
	var player = $("<audio class='player' preload='none' controls='controls'>").attr("src", audio);
	return player;
}


function info(msg) {
	$("#info").text(msg);
}

function fetchPlaylist(music, artistName) {
	info("Procurando pela musica " + music);
	$("#playlist").empty();
	var url;
	if(artistName!=null && artistName.length > 0) {
		url = 'http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&results=5&title=' + music +'&bucket=id:deezer&bucket=tracks&limit=true&artist=' + artistName;
	} else {
		url = 'http://developer.echonest.com/api/v4/song/search?api_key=9QB1EM63CLM2RR5V3&format=json&results=5&title=' + music +'&bucket=id:deezer&bucket=tracks&limit=true';
	}
	alert(url);
	jQuery.getJSON(url, 
			{ 
			},
			function(data) {
				info("");
				if (data.response.status.code == 0) {
					var songs = data.response.songs;
					
					addSongs(songs);
				} else {
					info("Can't create a playlist for " + music);
				}
			}
	);
}

function initUI() {
	$("#artist-name").keypress(
			function(e) {
				if(e.which == 13) {
					var artist = $("#artist-name").val();
					var music = artist.split('-')[0];
					var artistName = null;
					artistName = artist.split('-')[1];
					fetchPlaylist(music, artistName);
				}
			}
	);
}

$(document).ready(function() {
	fetchApiKey( function(api_key, isLoggedIn) {
		DEEZER_DEMO_API_KEY = api_key;
		initUI();
	});
});
