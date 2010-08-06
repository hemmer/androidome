{
	"patcher" : 	{
		"fileversion" : 1,
		"rect" : [ 2522.0, 344.0, 577.0, 353.0 ],
		"bglocked" : 0,
		"defrect" : [ 2522.0, 344.0, 577.0, 353.0 ],
		"openrect" : [ 0.0, 0.0, 0.0, 0.0 ],
		"openinpresentation" : 0,
		"default_fontsize" : 12.0,
		"default_fontface" : 0,
		"default_fontname" : "Arial",
		"gridonopen" : 0,
		"gridsize" : [ 15.0, 15.0 ],
		"gridsnaponopen" : 0,
		"toolbarvisible" : 1,
		"boxanimatetime" : 200,
		"imprint" : 0,
		"boxes" : [ 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpsend localhost 8000",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-7",
					"numoutlets" : 0,
					"fontsize" : 12.0,
					"patching_rect" : [ 261.0, 323.0, 137.0, 20.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "message",
					"text" : "maxpacketsize 4096",
					"numinlets" : 2,
					"fontname" : "Arial",
					"id" : "obj-13",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"fontsize" : 11.595187,
					"patching_rect" : [ 325.0, 225.0, 116.0, 18.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "message",
					"text" : "maxqueuesize 4096",
					"numinlets" : 2,
					"fontname" : "Arial",
					"id" : "obj-10",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"fontsize" : 11.595187,
					"patching_rect" : [ 325.0, 251.0, 112.0, 18.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "loadbang",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-6",
					"numoutlets" : 1,
					"outlettype" : [ "bang" ],
					"fontsize" : 12.0,
					"patching_rect" : [ 289.0, 185.0, 60.0, 20.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Intercepting outgoing values from the monome apps on port 8080.",
					"linecount" : 2,
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-5",
					"numoutlets" : 0,
					"fontsize" : 15.123713,
					"patching_rect" : [ 300.0, 30.0, 247.0, 41.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "sprintf host %s",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-4",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"fontsize" : 12.0,
					"patching_rect" : [ 30.0, 210.0, 89.0, 20.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "print Setup",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-2",
					"numoutlets" : 0,
					"fontsize" : 12.0,
					"patching_rect" : [ 30.0, 285.0, 69.0, 20.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "route /androidome/setup",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-1",
					"numoutlets" : 2,
					"outlettype" : [ "", "" ],
					"fontsize" : 12.0,
					"patching_rect" : [ 30.0, 165.0, 141.0, 20.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpsend 192.168.1.1 8080",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-16",
					"numoutlets" : 0,
					"fontsize" : 16.998821,
					"patching_rect" : [ 300.0, 285.0, 213.0, 26.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpreceive 8080",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-15",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"fontsize" : 16.998821,
					"patching_rect" : [ 300.0, 105.0, 134.0, 26.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Receiving incoming values from the androidome app on port 8000.",
					"linecount" : 2,
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-3",
					"numoutlets" : 0,
					"fontsize" : 15.123713,
					"patching_rect" : [ 30.0, 30.0, 242.0, 41.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "print OSC",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-8",
					"numoutlets" : 0,
					"fontsize" : 16.998821,
					"patching_rect" : [ 153.0, 318.0, 83.0, 26.0 ]
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpreceive 8001",
					"numinlets" : 1,
					"fontname" : "Arial",
					"id" : "obj-9",
					"numoutlets" : 1,
					"outlettype" : [ "" ],
					"fontsize" : 16.998821,
					"patching_rect" : [ 30.0, 90.0, 136.0, 26.0 ]
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"source" : [ "obj-1", 1 ],
					"destination" : [ "obj-7", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-15", 0 ],
					"destination" : [ "obj-8", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-9", 0 ],
					"destination" : [ "obj-1", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-6", 0 ],
					"destination" : [ "obj-13", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-6", 0 ],
					"destination" : [ "obj-10", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-1", 1 ],
					"destination" : [ "obj-8", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-1", 0 ],
					"destination" : [ "obj-4", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-4", 0 ],
					"destination" : [ "obj-2", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-15", 0 ],
					"destination" : [ "obj-16", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-4", 0 ],
					"destination" : [ "obj-16", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-10", 0 ],
					"destination" : [ "obj-16", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-13", 0 ],
					"destination" : [ "obj-16", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
 ]
	}

}
