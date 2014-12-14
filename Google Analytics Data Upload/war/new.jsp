<%@ page import="pageNumber.*, java.util.*, java.io.*, com.google.upload.*"%>

<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
    <body>
	    <form name="form1" action="/newsetup" method="post" >

        <h1>Setup New Account</h1>
        
        Client Id: <span class="info" style="font-style: italic">43326352718-08plo5mj..</span></br>
        
        <input name="client_id" type="text"></br>
        
        Client Secret: <span class="info" style="font-style: italic">79p9Vu1yK..</span></br>
        
        <input name="client_secret" type="text"></br>
        
        Redirect Uri: <span class="info" style="font-style: italic">79p9Vu1yK..</span></br>
        
        <input name="redirect_uri" type="text"></br>
                
		<input type="submit" name="submit" value="Submit Form">
		</form>
    </body>
</html>