
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

function clicarMusicas(musica)
{
    	musicaAux = musica;
    	//document.getElementById('hiddenFormPesquisa:idMusicaProcurada').value = musica;
    	//document.getElementById('hiddenFormPesquisa:cbPesquisaMusicaSistema').click(); 
    	setTimeout(redirecionarPagina, 1);
   
}

function redirecionarPagina()
{
	window.location.href = "/RecoMusic/procurarMusica/index.xhtml?t="+musicaAux;
}

function centerAndShowDialog(dialog)
{
    $(dialog).css("top",Math.max(0,(($(window).height() - $(dialog).outerHeight()) / 2) + $(window).scrollTop()) + "px");
    $(dialog).css("left",Math.max(0, (($(window).width() - $(dialog).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
    PF('dialogCadastro1').show();
}