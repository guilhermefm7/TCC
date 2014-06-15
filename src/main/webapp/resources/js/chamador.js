var DEEZER_DEMO_API_KEY='9QB1EM63CLM2RR5V3';

var audio
jQuery.ajaxSettings.traditional = true;


function createSongDiv(song) 
{
    var songDiv = $("<div class='sdiv' id=" + song.id + "-div> " + mostrarCurtir(song.id));
        
    return songDiv;
}

function mostrarCurtir(nome)
{
	document.getElementById('hiddenForm:IHIdMusica').value = nome;
	document.getElementById('hiddenForm:cbPesquisaCurtiu').click(); 
	
	if(document.getElementById('hiddenForm:IHCurtiuMusica').value=='true')
	{
		return "<div class=\"form-group\"><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"curtir('" + nome + "');\" class=\"btn-primary\"> <img id=\"btCurtirDescurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/linecons-free-vector-icons-pack/32/like-16.png\" /></a></div>";
	}
	else
	{
		return "<div class=\"form-group\"><a href=\"#\" style=\"padding:5px 20px;\" onclick=\"curtir('" + nome + "');\" class=\"btn-primary\"> <img id=\"btCurtirDescurtir" + nome + "\" src=\"https://cdn3.iconfinder.com/data/icons/stroke/53/Unlike-16.png\"/></a></div>";
	}
}

function curtir(nome)
{
	
	document.getElementById('hiddenForm:IHIdMusica').value = nome;
	document.getElementById('hiddenForm:cbAvaliaMusica').click(); 
	
	if(document.getElementById('hiddenForm:IHCurtiuMusica').value=='true')
	{
		document.getElementById('btCurtirDescurtir' + nome).src = "https://cdn3.iconfinder.com/data/icons/linecons-free-vector-icons-pack/32/like-16.png";
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


function fetchDeezerTrack(song, div) {
    if (song.tracks.length > 0) {
        var tid = song.tracks[0].foreign_id.split(':')[2];
        var url = 'http://api.deezer.com/2.0/track/' + tid + '?callback=?'

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
    if(artistName!=null && artistName.length > 0) {
    		var url = 'http://developer.echonest.com/api/v4/song/search?api_key=FILDTEOIK2HBORODV&format=json&results=5&title=' + music +'&bucket=id:deezer&bucket=tracks&limit=true&artist=' + artistName
    	} else {
	    	var url = 'http://developer.echonest.com/api/v4/song/search?api_key=FILDTEOIK2HBORODV&format=json&results=5&title=' + music +'&bucket=id:deezer&bucket=tracks&limit=true'
    	}
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














/*


var DEEZER_DEMO_API_KEY='9QB1EM63CLM2RR5V3';

var audio
jQuery.ajaxSettings.traditional = true;


function createSongDiv(song) {
    var songDiv = $("<div class='sdiv' id=" + song.id + "-div>");
    return songDiv;
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


function fetchDeezerTrack(song, div) {
    if (song.tracks.length > 0) {
        var tid = song.tracks[0].foreign_id.split(':')[2];
        var url = 'http://api.deezer.com/2.0/track/' + tid + '?callback=?'

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
                div.append($('<h:panelGroup layout="block" class="form-group" id="btCurtirMusica">'));
                div.append($('<h:commandLink type="submit" class="btn-primary" style="padding:5px 20px;" value="Curtir Musica" actionListener="#{MusicaBean.avaliarMusica}" />'));
                div.append($('</h:panelGroup>'));
                
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
    if(artistName!=null && artistName.length > 0) {
    		var url = 'http://developer.echonest.com/api/v4/song/search?api_key=FILDTEOIK2HBORODV&format=json&results=5&title=' + music +'&bucket=id:deezer&bucket=tracks&limit=true&artist=' + artistName
    	} else {
	    	var url = 'http://developer.echonest.com/api/v4/song/search?api_key=FILDTEOIK2HBORODV&format=json&results=5&title=' + music +'&bucket=id:deezer&bucket=tracks&limit=true'
    	}
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

*/