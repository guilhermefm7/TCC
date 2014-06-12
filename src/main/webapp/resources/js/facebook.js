 FB.init({ 
        appId: '577169682402524',          //ID da aplicação web definido no Facebook
        channelUrl : 'http://localhost:8080/RecoMusic/index.xhtml', // Channel File
        cookie: true,                               // permitir cookies para poder acessar a sessão
        status: true,                               // verificar o status do login
        xfbml: true,                              // usar ou não tags do Facebook
        oauth: true                                // autenticação via OAuth 2.0
        
        
     });


    function loginWithFacebook()
    {
        FB.login(function(response) 
        {
           if (response.authResponse) 
           {
            	window.location = "http://localhost:8080/RecoMusic/index.xhtml?token="+response.authResponse.accessToken;
           }
           else
           {
        	   console.log('User cancelled login or did not fully authorize.');
           }
        }, {scope: 'email,user_likes,user_actions.music'});
    } 
 
  (function(d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s); js.id = id;
      js.src = "//connect.facebook.net/pt_BR/sdk.js#xfbml=1&appId=577169682402524&version=v2.0";
      fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));
  
/*    var el = document.getElementById('share-button');
    el.onclick = function (e) {
    	e.preventDefault();
    	FB.ui({
    		method: 'feed',
    		name: 'RecoMusic',
    		link: ' http://www.devmedia.com.br/',
    		picture: 'http://img2.wikia.nocookie.net/__cb20100528223948/wikinaruto/pt/images/0/03/Mangekyou_Madara_Icone.png',
    		caption: 'Come as You Are',
    		description: 'Nirvana - Come as you are'
    	});
    }*/
    
    
    function openFbPopUp() {
        FB.ui(
          {
            method: 'feed',
            name: 'Facebook Dialogs',
            link: 'https://developers.facebook.com/docs/dialogs/',
            picture: 'http://fbrell.com/f8.jpg',
            caption: 'Reference Documentation',
            description: 'Dialogs provide a simple, consistent interface for applications to interface with users.'
          },
          function(response) {
            if (response && response.post_id) {
              alert('Post was published.');
            } else {
              alert('Post was not published.');
            }
          }
        );
    }

    
    
    function openFbPopUp() {
        var fburl = 'http://54.251.121.6/cnc/cupcakes-n-chai-side-table';
        var fbimgurl = 'http://imageURL';
        var fbtitle = 'Your title';
        var fbsummary = "This is the description blah blah blah";
        var sharerURL = "http://www.facebook.com/sharer/sharer.php?s=100&p[url]=" + encodeURI(fburl) + "&p[images][0]=" + encodeURI(fbimgurl) + "&p[title]=" + encodeURI(fbtitle) + "&p[summary]=" + encodeURI(fbsummary);
        window.open(
          sharerURL,
          'facebook-share-dialog', 
          'width=626,height=436'); 
        return  false;
    }