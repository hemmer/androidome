{
	"patcher" : 	{
		"fileversion" : 1,
		"rect" : [ 2140.0, 125.0, 473.0, 243.0 ],
		"bglocked" : 0,
		"defrect" : [ 2140.0, 125.0, 473.0, 243.0 ],
		"openrect" : [ 0.0, 0.0, 0.0, 0.0 ],
		"openinpresentation" : 1,
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
					"maxclass" : "comment",
					"text" : "Setup Messages\nto Androidome",
					"linecount" : 2,
					"fontsize" : 12.0,
					"id" : "obj-22",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 487.0, 243.0, 150.0, 34.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "LED Messages to Androidome",
					"linecount" : 2,
					"fontsize" : 12.0,
					"id" : "obj-21",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 292.0, 496.0, 150.0, 34.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Button presses to Max/Live/Chuck etc",
					"linecount" : 2,
					"fontsize" : 12.0,
					"id" : "obj-20",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 540.0, 180.0, 150.0, 34.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Enable Logging\nOSC Messages",
					"linecount" : 2,
					"presentation_linecount" : 2,
					"presentation" : 1,
					"fontsize" : 16.0,
					"id" : "obj-19",
					"presentation_rect" : [ 18.0, 178.0, 124.0, 43.0 ],
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 15.0, 150.0, 196.0, 43.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "button",
					"presentation" : 1,
					"outlettype" : [ "bang" ],
					"id" : "obj-18",
					"presentation_rect" : [ 269.0, 72.0, 46.0, 46.0 ],
					"numinlets" : 1,
					"patching_rect" : [ 269.0, 72.0, 46.0, 46.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "gate 1",
					"outlettype" : [ "" ],
					"fontsize" : 12.0,
					"id" : "obj-17",
					"numinlets" : 2,
					"fontname" : "Arial",
					"patching_rect" : [ 150.0, 285.0, 44.0, 20.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "gate 1",
					"outlettype" : [ "" ],
					"fontsize" : 12.0,
					"id" : "obj-14",
					"numinlets" : 2,
					"fontname" : "Arial",
					"patching_rect" : [ 390.0, 270.0, 44.0, 20.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "toggle",
					"presentation" : 1,
					"outlettype" : [ "int" ],
					"id" : "obj-12",
					"presentation_rect" : [ 150.0, 183.0, 30.0, 30.0 ],
					"numinlets" : 1,
					"patching_rect" : [ 217.0, 149.0, 23.0, 23.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "button",
					"presentation" : 1,
					"outlettype" : [ "bang" ],
					"id" : "obj-11",
					"presentation_rect" : [ 270.0, 15.0, 46.0, 46.0 ],
					"numinlets" : 1,
					"patching_rect" : [ 270.0, 15.0, 45.0, 45.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpsend localhost 8000",
					"fontsize" : 12.0,
					"id" : "obj-7",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 538.0, 159.0, 137.0, 20.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "message",
					"text" : "maxpacketsize 4096",
					"outlettype" : [ "" ],
					"fontsize" : 11.595187,
					"id" : "obj-13",
					"numinlets" : 2,
					"fontname" : "Arial",
					"patching_rect" : [ 330.0, 406.0, 116.0, 18.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "message",
					"text" : "maxqueuesize 4096",
					"outlettype" : [ "" ],
					"fontsize" : 11.595187,
					"id" : "obj-10",
					"numinlets" : 2,
					"fontname" : "Arial",
					"patching_rect" : [ 330.0, 432.0, 112.0, 18.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "loadbang",
					"outlettype" : [ "bang" ],
					"fontsize" : 12.0,
					"id" : "obj-6",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 330.0, 371.0, 60.0, 20.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Intercepting outgoing values from the monome apps on port 8080.",
					"linecount" : 2,
					"presentation_linecount" : 2,
					"presentation" : 1,
					"fontsize" : 15.123713,
					"id" : "obj-5",
					"presentation_rect" : [ 15.0, 75.0, 247.0, 41.0 ],
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 15.0, 75.0, 247.0, 41.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "sprintf host %s",
					"outlettype" : [ "" ],
					"fontsize" : 12.0,
					"id" : "obj-4",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 390.0, 240.0, 89.0, 20.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "print Setup",
					"fontsize" : 12.0,
					"id" : "obj-2",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 390.0, 300.0, 69.0, 20.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "route /androidome/setup",
					"outlettype" : [ "", "" ],
					"fontsize" : 12.0,
					"id" : "obj-1",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 390.0, 135.0, 141.0, 20.0 ],
					"numoutlets" : 2
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpsend 192.168.1.1 8080",
					"fontsize" : 16.998821,
					"id" : "obj-16",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 277.0, 454.0, 213.0, 26.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpreceive 8080",
					"outlettype" : [ "" ],
					"fontsize" : 16.998821,
					"id" : "obj-15",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 390.0, 45.0, 134.0, 26.0 ],
					"numoutlets" : 1
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "comment",
					"text" : "Receiving incoming values from the androidome app on port 8001.",
					"linecount" : 2,
					"presentation_linecount" : 2,
					"presentation" : 1,
					"fontsize" : 15.123713,
					"id" : "obj-3",
					"presentation_rect" : [ 15.0, 15.0, 242.0, 41.0 ],
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 15.0, 15.0, 242.0, 41.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "print OSC",
					"fontsize" : 16.998821,
					"id" : "obj-8",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 150.0, 315.0, 83.0, 26.0 ],
					"numoutlets" : 0
				}

			}
, 			{
				"box" : 				{
					"maxclass" : "newobj",
					"text" : "udpreceive 8001",
					"outlettype" : [ "" ],
					"fontsize" : 16.998821,
					"id" : "obj-9",
					"numinlets" : 1,
					"fontname" : "Arial",
					"patching_rect" : [ 389.0, 10.0, 136.0, 26.0 ],
					"numoutlets" : 1
				}

			}
 ],
		"lines" : [ 			{
				"patchline" : 				{
					"source" : [ "obj-12", 0 ],
					"destination" : [ "obj-17", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-17", 0 ],
					"destination" : [ "obj-8", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-15", 0 ],
					"destination" : [ "obj-17", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-1", 1 ],
					"destination" : [ "obj-17", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-14", 0 ],
					"destination" : [ "obj-2", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-12", 0 ],
					"destination" : [ "obj-14", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-4", 0 ],
					"destination" : [ "obj-14", 1 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-9", 0 ],
					"destination" : [ "obj-11", 0 ],
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
					"source" : [ "obj-4", 0 ],
					"destination" : [ "obj-16", 0 ],
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
					"source" : [ "obj-1", 0 ],
					"destination" : [ "obj-4", 0 ],
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
					"source" : [ "obj-6", 0 ],
					"destination" : [ "obj-13", 0 ],
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
					"source" : [ "obj-1", 1 ],
					"destination" : [ "obj-7", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
, 			{
				"patchline" : 				{
					"source" : [ "obj-15", 0 ],
					"destination" : [ "obj-18", 0 ],
					"hidden" : 0,
					"midpoints" : [  ]
				}

			}
 ]
	}

}
