
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
    if(($(window).width())<=600)
    {
    	$(dialog).css("top",Math.max(0,(($(window).height() - $(dialog).outerHeight()) / 1.5) ) + "px");
    	$(dialog).css("left",Math.max(0, (($(window).width() - $(dialog).outerWidth()) / 5) ) + "px");
    	$(dialog).css("width",Math.max(0, (($(window).width() ) / 1.5) ) + "px");
    	$(dialog).css("height",Math.max(0, (($(window).height() ) / 1.25) ) + "px");
    }
    PF('dialogCadastro1').show();
}

function centerAndShowDialog1()
{
	var dialog  = document.getElementById('dialogCadastro1');
	if(($(window).width())<=600)
	{
		$(dialog).css("top",Math.max(0,(($(window).height() - $(dialog).outerHeight()) / 1.5) ) + "px");
		$(dialog).css("left",Math.max(0, (($(window).width() - $(dialog).outerWidth()) / 5) ) + "px");
		$(dialog).css("width",Math.max(0, (($(window).width() ) / 1.5) ) + "px");
		$(dialog).css("height",Math.max(0, (($(window).height() ) / 1.25) ) + "px");
	}
	PF('dialogCadastro1').show();
}