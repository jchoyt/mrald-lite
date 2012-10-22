// ===================================================================
// THis will allow for a tree view that can have branches that
// open and close
// ===================================================================
        function checkForm(field)
        {
            var val = field.value;
          
          if( (!val) )
            {
                alert ("\nYou must enter a valid " + field.name + " to continue.")
                return false;
            }
       
            else return true;
        }
	
	function checkForm(field, label)
        {
            var val = field.value;
          
          if( (!val) )
            {
                alert ("\nYou must enter a valid " + label + " to continue.")
                return false;
            }
       
            else return true;
        }
