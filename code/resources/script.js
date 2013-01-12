	 
	 
	var steps = [];
	var minStep, maxStep = -1;
	
	var holder = null,
		//tests = {}, 
		acceptedTypes = {
							'image/png': true,
							'image/jpeg': true,
							'image/gif': true
						},
		//progress = null,
		fileupload = null,
		formData = null,
		fileQueue = [];

	/*
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
	*/
	
	function sendAJAX( args )
	{
		var xmlhttp;
		if (window.XMLHttpRequest)
		{// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		}
		else
		{// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xmlhttp.onreadystatechange=function()
		{
			if (xmlhttp.readyState==4 && xmlhttp.status==200)
			{
				if( !("after" in args) )
				{
					return;
				}
				
				args.after( xmlhttp );
			}
		}
		
		xmlhttp.open( "POST", args.address, true);
		xmlhttp.send( args.data );
	}
	
	var stepHasASelectedOption = function( stepNr )
	{
		for( var i = 0; i < steps[stepNr].options.length; i++ )
		{
			if( !steps[stepNr].options[i].selected )
			{
				continue;
			}
			
			return true;
		}
		
		return false;
	}
	
	//behavior of an option
	var processOption = function( args )
	{
		//deselect option
		var optionObj = steps[args.step].options[args.option];
		
		if( optionObj.selected )
		{
			optionObj.container.className = "option";
			optionObj.selected = false;
			
			//reset parameters
			for( var i = 0; i < optionObj.parameters.length; i++ )
			{
				optionObj.parameters[i].value = "";
			}
			
			//adjust steps if needed
			if( minStep == args.step )
			{
				minStep = steps.length;
				
				for( var i = args.step + 1; i < steps.length; i++ )
				{
					if( !stepHasASelectedOption( i ) )
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
					if( !stepHasASelectedOption( i ) )
					{
						continue;
					}
					
					maxStep = i;
					break;
				}
			}
			
			return;
		}
		
		//check to see if similar option is selected
		for( var i = 0; i < steps[ args.step ].options.length; i++ )
		{
			if( !steps[ args.step ].options[i].selected )
			{
				continue;
			}
			/*
			if( steps[ args.step ].options[i].optionType == optionObj.optionType )
			{
				alert("Option with similar effect already selected");
				
				return;
			}
			*/
			break;
		}
		
		if( args.step > maxStep )
		{
			maxStep = args.step;
		}
		
		if( args.step < minStep )
		{
			minStep = args.step;
		}
		
		optionObj.container.className = "selectedOption";
		optionObj.selected = true;
	}
	
	//open xml file and use it to create menu
	var setupStepsAndOptions = function()
	{
		sendAJAX({ 
					address: "http://localhost/Paws/configFile.xml"
					data: "command=getInitConfig"
					after: function( xmlhttp )
					{
						var xmlDoc = xmlhttp.responseXML;
						
						var optionsDiv = kat.getElm("options");

						var addEventToOption = function( args )
						{
							kat.addEvent( newDivOption, "click", function(e)
							{
								e.preventDefault();
								
								processOption( args )
							}, false);
							
							kat.addEvent( newDivOption, "dblclick", function(e)
							{
								e.preventDefault();
							}, false);
						}
						
						var addEventToParameter = function( args )
						{
							kat.addEvent( args.paramObj, "click", function(e)
							{
								if( e.stopPropagation )
								{
									e.stopPropagation();
								}
								else
								{
									e.cancelBubble = true;
								}
								
								if( steps[args.step].options[args.option].selected )
								{
									return;
								}
								
								processOption({
												step: args.step,
												option: args.option
											});
							}, false);
						}
						
						var xmlSteps = xmlDoc.getElementsByTagName("execType");
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
										options: []
									};
							
							var xmlOptions = xmlSteps[i].getElementsByTagName("execName");
							//for each option of step
							for( var j = 0; j < xmlOptions.length; j++ )
							{
								var newDivOption = kat.createElm("div", newDivStep);
								newDivOption.appendChild( document.createTextNode( xmlOptions[j].getAttribute("name") ) );
								kat.createElm( "br", newDivOption );
								
								var xmlParams = xmlOptions[j].getElementsByTagName("parameter");
								//for each parameter of option
								var newParams = [];
								for( var k = 0; k < xmlParams.length; k++ )
								{
									newDivOption.appendChild( document.createTextNode(xmlParams[k].getAttribute("name")) );
									var newParam = kat.createElm("input", newDivOption);
									newParam.setAttribute( "type", "text" );
									newParam.setAttribute( "name", xmlParams[k].getAttribute("name") );
									addEventToParameter({
															step: i,
															option: j,
															paramObj: newParam
														});
														
									newParams.push(newParam);
								}
								
								newDivOption.className = "option";
								
								addEventToOption({ 
													step: i,
													option: j
												});
								
								steps[i].options[j] = {
														selected: false,
														optionName: xmlOptions[j].getAttribute("name"),
														//optionType: xmlOptions[j].getAttribute("type"),
														container: newDivOption,
														parameters: newParams
													};
							}
						}
					}
				});
	}
	
	var createWorkflowXML = function()
	{
		var workflow = document.createElement( "workflow" );
		for( var i = minStep; i <= maxStep; i++ )
		{
			for( var j = 0; j < steps[i].options.length; j++ )
			{
				if( !steps[i].options[j].selected )
				{
					continue;
				}
				
				var option = kat.createElm( "option", workflow );
				//option.setAttribute( "name", steps[i].options[j].optionName );
				
				var optionName = kat.createElm( "optionName", option );
				optionName.innerHTML = steps[i].options[j].optionName;
				
				for( var k = 0; k < steps[i].options[j].parameters.length; k++ )
				{
					if( steps[i].options[j].parameters[k].value == "" )
					{
						alert( "Parameter not filled in" );
						
						steps[i].options[j].parameters[k].focus();
						
						return null;
					}
					
					var parameter = kat.createElm( "parameter", option );
					//parameter.setAttribute( "name", steps[i].options[j].parameters[k].getAttribute("name") );
					parameter.innerHTML = steps[i].options[j].parameters[k].getAttribute("name") + ', ' + steps[i].options[j].parameters[k].value;
				}
			}
		}
		
		return workflow;
	}
	
	/*
	var previewFile = function(file)
	{
		if(	tests.filereader === true
			&& acceptedTypes[file.type] === true
		)
		{
			var reader = new FileReader();
			reader.onload = function (event)
			{
				var image = new Image();
				image.src = event.target.result;
				image.width = 120; // a fake resize
				holder.appendChild(image);
			}

			reader.readAsDataURL(file);
		} 
		else
		{
			holder.innerHTML += '<p>Uploaded ' + file.name + ' ' + (file.size ? (file.size/1024|0) + 'K' : '');
			console.log(file);
		}
	}*/
	
	var showFileInList = function (ev)
	{
        var file = ev.target.file;
        if (file)
		{
            var image = new Image();
			image.src = ev.target.result;
			image.width = 120; // a fake resize
			holder.appendChild(image);
			
            fileQueue.push({
                file : file,
            });
        }
    }
	
	var readImageFiles = function(files)
	{
		//debugger;
        for (var i = 0; i < files.length; i++)
		{
			if( acceptedTypes[files[i].type] === false )
			{
				continue;
			}
            var fr = new FileReader();
            fr.file = files[i];
            fr.onloadend = showFileInList;
            fr.readAsDataURL(files[i]);
        }
	}
	
	var uploadImageFile = function (file) {
        if (file) {
            var xhr = new XMLHttpRequest(),
                upload = xhr.upload;
            upload.addEventListener("progress", function (ev) {
                if (ev.lengthComputable) {
                    
                }
            }, false);
            upload.addEventListener("load", function (ev) {
               
            }, false);
            upload.addEventListener("error", function (ev) {console.log(ev);}, false);
            xhr.open(
                "POST",
                "upload.php"
            );
            xhr.setRequestHeader("Cache-Control", "no-cache");
            xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            xhr.setRequestHeader("X-File-Name", file.name);
            xhr.send(file);
        }
    }
	
	var sendImages = function()
	{
		// now post a new XHR request
		console.log("sending");
		
		while (fileQueue.length > 0) {
            var item = fileQueue.pop();
            
            uploadImageFile(item.file);
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
			
			var workflow = createWorkflowXML();
			
			if( !workflow )
			{
				return;
			}
			
			console.log( workflow );
			/*
			sendAJAX({
						address: "",
						data: (new XMLSerializer()).serializeToString(workflow)
						after: function()
						{
							for(  )
						}
					});
			*/
		}, false);
		
		
		//for debugging purposes
		kat.addEvent( window, "keypress", function(e)
		{
			if( e.keyCode == 97 )//a
			{
				console.log( minStep, maxStep );
			}
			if( e.keyCode == 98 )//a
			{
				sendImages();
			}
		}, false);
		
		
		//D&D upload
		holder = document.getElementById('holder');
		holder.ondrop = function (e)
		{
			e.preventDefault();
			readImageFiles(e.dataTransfer.files);
		}
		/*
		tests = {
					filereader: typeof FileReader != 'undefined',
					dnd: 'draggable' in document.createElement('span'),
					formdata: !!window.FormData,
					progress: "upload" in new XMLHttpRequest
				};
		*/				
		//progress = document.getElementById('uploadprogress');
		fileupload = document.getElementById('upload');
		
		//formData = tests.formdata ? new FormData() : null;
		/*
		if (tests.dnd)
		{ 
			holder.ondragover = function ()
			{
				this.className = 'hover';
				return false;
			}
			holder.ondragend = function ()
			{
				this.className = '';
				return false;
			}
			
		}
		else
		{
			fileupload.className = 'hidden';
			fileupload.querySelector('input').onchange = function ()
			{
				readImageFiles(this.files);
			};
		}
		*/
		
		kat.getElm("fileField").onchange = this.readImageFiles;
	}