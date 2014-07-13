
var musicaAux;

function procurarMusicas(musica, event)
{
    var nKeyCode = event.keyCode; 
    if(nKeyCode == 13)
    { 
    	musicaAux = musica;
    	//document.getElementById('hiddenFormPesquisa:idMusicaProcurada').value = musica;
    	//document.getElementById('hiddenFormPesquisa:cbPesquisaMusicaSistema').click(); 
    	setTimeout(redirecionarPagina, 1);
    }
}

function redirecionarPagina()
{
	window.location.href = "/RecoMusic/procurarMusica/index.xhtml?t="+musicaAux;
}
