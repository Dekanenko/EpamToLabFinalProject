<%@ attribute name="locale" required="false" %>

<%! String path = "images/";%>

<% if(locale.length()<1){
        path += "en.png";
    }else{
        path += locale+".png";
}%>

<img src=<%=path%> class="img">


