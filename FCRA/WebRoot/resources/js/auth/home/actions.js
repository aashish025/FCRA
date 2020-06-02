$(document).ready(function(){  
    
    // setting for list with button <<more>>        
    var cList = 5; // count list items   
    
    
    if($(".adminControl").hasClass('active')){
        $('.admin').fadeIn(300);
    }
    
    
    $(".adminControl").click(function(){
        
        if($(this).hasClass('active')){
            
            //$.cookies.set('b_Admin_visibility','hidden');
            
            $('.admin').fadeOut(200);
            
            $(this).removeClass('active');
            
        }else{
            
            //$.cookies.set('b_Admin_visibility','visible');
            
            $('.admin').fadeIn(300);
            
            $(this).addClass('active');
        }
        
    });
    
    
    $(".navigation .openable > a").click(function(){
        var par = $(this).parent('.openable');
        var sub = par.find("ul");

        if(sub.is(':visible')){
            par.find('.popup').hide();
            par.removeClass('active');            
        }else{
        	$(".navigation .openable").each(function(){
        		$(this).removeClass('active');
        	});
            par.addClass('active');            
        }
        
        return false;
    });
    
    $(".jbtn").button();
    
    $(".alert").click(function(){
        $(this).fadeOut(300, function(){            
                $(this).remove();            
        });
    });
    
    $(".buttons li > a").click(function(){
        
        var parent   = $(this).parent();
        
        if(parent.find(".dd-list").length > 0){
        
            var dropdown = parent.find(".dd-list");

            if(dropdown.is(":visible")){
                dropdown.hide();
                parent.removeClass('active');
            }else{
                dropdown.show();
                parent.addClass('active');
            }

            return false;
            
        }
        
    });


});

