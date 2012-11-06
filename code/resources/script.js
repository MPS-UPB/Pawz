	 
	 
	var steps = [];
	var minStep, maxStep = -1;
	
	 //open xml file for reading and return dom structure
	var readXMLFile = function( filename )
	 {
		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		}
		else
		{// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.open("GET", filename, false);
		xmlhttp.send();
		
		return xmlhttp.responseXML;
	}

	//behavior of an option
	var processOption = function( optionDiv, args )
	{
		//deselect option
		if( steps[args.step].selectedOption == args.option )
		{
			optionDiv.className = "option";
			steps[args.step].selectedOption = -1;
			
			
			//adjust steps if needed
			if( minStep == args.step )
			{
				minStep = steps.length;
				
				for( var i = args.step + 1; i < steps.length; i++ )
				{
					if( steps[i].selectedOption == -1 )
					{
						continue;
					}
					
					minStep = i;
					break;
				}
			}
			
			if(  maxStep == args.step )
			{
				maxStep = -1;
				
				for( var i = args.step - 1; i >= 0; i-- )
				{
					if( steps[i].selectedOption == -1 )
					{
						continue;
					}
					
					maxStep = i;
					break;
				}
			}
			
			return;
		}
		
		if( args.step > maxStep )
		{
			maxStep = args.step;
		}
		
		if( args.step < minStep )
		{
			minStep = args.step;
		}
		
		if(steps[args.step].selectedOption != -1)
		{
			steps[args.step].options[steps[args.step].selectedOption].className = "option";
		}
		
		optionDiv.className = "selectedOption";
		steps[args.step].selectedOption = args.option;
	}
	
	//open xml file and use it to create menu
	var setupStepsAndOptions = function()
	{
		var xmlDoc = readXMLFile( "resources/content.xml" );
		
		var optionsDiv = kat.getElm("options");
		
		var addEventToOption = function( args )
		{
			kat.addEvent( newDivOption, "click", function(e)
			{
				e.preventDefault();
				
				processOption( e.target, args )
			}, false);
			
			kat.addEvent( newDivOption, "dblclick", function(e)
			{
				e.preventDefault();
			}, false);
		}
		
		
		var xmlSteps = xmlDoc.getElementsByTagName("step");
		minStep = xmlSteps.length;
		//for each step
		for( var i = 0; i < xmlSteps.length; i++ )
		{
			var newDivStep = kat.createElm("div", optionsDiv);
			newDivStep.className = "step";
			
			var newDivStepName = kat.createElm("div", newDivStep);
			newDivStepName.className = "stepName"
			newDivStepName.appendChild( document.createTextNode(xmlSteps[i].getAttribute("name") ) );
			
			steps[i] = {
								selectedOption: -1,
								options: []
							};
			
			var xmlOptions = xmlSteps[i].getElementsByTagName("option");
			//for each option of step
			for( var j = 0; j < xmlOptions.length; j++ )
			{
				var newDivOption = kat.createElm("div", newDivStep);
				newDivOption.appendChild( document.createTextNode( xmlOptions[j].getAttribute("name") ) );
				newDivOption.className = "option";
				
				addEventToOption({ 
												step: i,
												option: j
											});
				
				steps[i].options[j] = newDivOption;
			}
		}
	}
	
	//window loaded
	window.onload = function()
	{
		var leftSurface = kat.getElm("leftSurface"),
			loadButton = kat.getElm("loadButton"),
			theFile = kat.getElm("theFile");
		
		setupStepsAndOptions()
		
		//open file browser - only works in chrome
		kat.addEvent( loadButton, "click", function(e)
		{
			var evt = document.createEvent("MouseEvents");
			evt.initEvent("click", true, false);
			theFile.dispatchEvent(evt);
		}, false);
		
		
		//create send button and set up
		var sendButton = kat.createElm("input", leftSurface);
		sendButton.setAttribute("type", "button");
		sendButton.setAttribute("value", "Send workflow to server");
		sendButton.className = "sendButton";
		kat.addEvent( sendButton, "click", function(e)
		{
			if( maxStep == -1 )
			{
				alert("No option selected");
				
				return;
			}
			
			for( var i = minStep + 1; i < maxStep; i++ )
			{
				if( steps[i].selectedOption != -1 )
				{
					continue;
				}
				
				alert("Gap in steps");
				
				return;
			}
			
			alert("All ok");
		}, false);
		
		
		//for debugging purposes
		kat.addEvent( window, "keypress", function(e)
		{
			if( e.keyCode == 97 )//a
			{
				console.log( minStep, maxStep );
			}
		}, false);
	}