
	var kat = 
	{
		getMouseXY: function(e)
		{
			if( e.clientX )
			{
				kat.getMouseXY = function(e)
				{
					return {
									x: e.clientX + document.body.scrollLeft,
									y: e.clientY + document.body.scrollTop
								};
				}
			}
			else
			{
				kat.getMouseXY = function(e)
				{
					return {
									x: e.pageX,
									y: e.pageY
								};
				}
			}
			
			return kat.getMouseXY(e);
		},
		
		
		getMouseInElementXY: function( e )
		//not tested
		{
			var p = kat.getMouseXY(e);
			return {
							x: p.x - e.target.offsetLeft,
							y: p.y - e.target.offsetTop
						};
		},
		
		
		addEvent: function( elm, evt, fct, bbl )
		{
			if( elm.addEventListener )
			{
				kat.addEvent = function( elm, evt, fct, bbl )
				{
					elm.addEventListener( evt, fct, bbl );
				}
			}
			else
			{
				kat.addEvent = function( elm, evt, fct )
				{
					elm.attachEvent( "on" + evt, fct );
				}
			}
			
			kat.addEvent( elm, evt, fct, bbl );
		},
		
		
		removeEvent: function( elm, evt, fct, bbl )
		{
			if( elm.addEventListener )
			{
				kat.removeEvent = function( elm, evt, fct, bbl )
				{
					elm.addEventListener( evt, fct, bbl );
				}
			}
			else
			{
				kat.removeEvent = function( elm, evt, fct )
				{
					elm.detachEvent( "on" + evt, fct );
				}
			}
			
			kat.removeEvent( elm, evt, fct, bbl );
		},
		
		
		//
		//because i'm lazy
		//
		getElm: function( id )
		{
			return document.getElementById( id );
		},
		
		createElm: function( name, parent )
		{
			var elm = document.createElement( name );
			
			if( parent )
			{
				parent.appendChild( elm );
			}
			
			return elm;
		}
	}

/*
	var kat = 
	{
		IE:
		{
			getMouseXY: function( e )
			{
				return {
								x: e.pageX,
								y: e.pageY
							};
			},
			
			getMouseInElementXY: function( e )
			{
				return {
								x: e.pageX,
								y: e.pageY
							};
			},
			
			addEvent: function( elm, evt, fct )
			{
				elm.attachEvent( "on" + evt, fct );
			}
		},
		
		FF:
		{
			getMouseXY: function( e )
			{
				return {
								x: e.clientX + document.body.scrollLeft,
								y: e.clientY + document.body.scrollTop
							};
			},
			
			getMouseInElementXY: function( e )
			{
				return {
								x: e.clientX + document.body.scrollLeft - e.target.offsetLeft,
								y: e.clientY + document.body.scrollTop - e.target.offsetTop
							};
			}
			
			addEvent: function( elm, evt, fct, bbl )
			{
				elm.addEventListener( evt, fct, bbl );
			}
		},
		
		init: function()
		{
			
			if( document.all )
			{
				//is IE
				
				for( var i in kat.IE )
				{
					kat[i] = kat.IE[i];
				}
			}
			else
			{
				for( var i in kat.FF )
				{
					kat[i] = kat.FF[i];
				}
			}
				
			delete kat.IE;
			delete kat.FF;
			delete kat.init;
		}
	}
*/