// ===================================================================
// THis will allow for a tree view that can have branches that
// open and close
// ===================================================================

function toggle(nodeId, imageDir)
{
	
	 var node = document.getElementById(nodeId);
	 //node.style.display ='block';

	// Unfold the branch if it isn't visible
	if (node.style.display == 'none')
	{
		 var imageNode = document.getElementById(nodeId + "Image");
	 
	        // Change the image (if there is an image)
		if (imageNode != null)
		{
				imageNode.src = imageDir + "minus.gif";
			
		}
		node.style.display = 'block';
	}
	// Collapse the branch if it IS visible
	else
	{
	// find the next DIV	   
	   	// Change the image (if there is an image)
		
		 var imageNode = document.getElementById(nodeId + "Image");
	 
	        // Change the image (if there is an image)
		if (imageNode != null)
		{
				imageNode.src = imageDir + "plus.gif";
			
		}
		node.style.display = 'none';
	}
}
