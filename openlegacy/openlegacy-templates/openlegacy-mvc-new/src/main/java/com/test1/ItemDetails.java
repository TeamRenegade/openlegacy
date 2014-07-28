package com.test1;

import org.openlegacy.annotations.screen.*;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.definitions.EnumGetValue;  

@ScreenEntity()
@ScreenIdentifiers(identifiers = { 
				@Identifier(row = 2, column = 26, value = "    Item Details     "), 
				@Identifier(row = 4, column = 2, value = "Type choices, press Enter."), 
				@Identifier(row = 6, column = 2, value = "Item Number . . . . . . . . .") 
				})
@ScreenActions(actions = { 
				@Action(action = TerminalActions.F1.class, displayName = "Help", alias = "help"), 
				@Action(action = TerminalActions.F4.class, displayName = "Prompt", alias = "prompt"), 
				@Action(action = TerminalActions.F12.class, displayName = "Cancel", alias = "cancel"), 
				@Action(action = TerminalActions.F2.class ,additionalKey= AdditionalKey.SHIFT, displayName = "Delete", alias = "delete") 
				})
@ScreenNavigation(accessedFrom = Items.class 
					, drilldownValue="2", requiresParameters=true)
public class ItemDetails {
    
	
	@ScreenField(key = true, row = 6, column = 33, endColumn= 47 ,labelColumn= 2 ,editable= true ,displayName = "Item Number", sampleValue = "2000")
    private Integer itemNumber;
	
	@ScreenField(row = 7, column = 33, endColumn= 72 ,labelColumn= 2 ,editable= true ,displayName = "Item Description", sampleValue = "Domino Cubes - Board Games")
    private String itemDescription;
	
	@ScreenField(row = 8, column = 33, endColumn= 47 ,labelColumn= 2 ,editable= true ,displayName = "Substitute item number")
    private String substituteItemNumber;
	
	@ScreenField(row = 9, column = 33, endColumn= 47 ,labelColumn= 2 ,editable= true ,displayName = "Item weight", sampleValue = "850", helpText = "GR")
    private Integer itemWeight;
	
	@ScreenField(row = 11, column = 33, endColumn= 35 ,labelColumn= 2 ,editable= true ,displayName = "Stock Group", sampleValue = "SG", helpText = "SG=Standard group CG=Custom Group")
    private StockGroup stockGroup;
	
	@ScreenField(row = 12, column = 33, endColumn= 37 ,labelColumn= 2 ,editable= true ,displayName = "Unit of Measure")
    private String unitOfMeasure;
	
	@ScreenField(row = 13, column = 33, endColumn= 33 ,labelColumn= 2 ,editable= true ,displayName = "Pallet label required")
    private String palletLabelRequired;
	
	@ScreenField(row = 14, column = 33, endColumn= 34 ,labelColumn= 2 ,editable= true ,displayName = "VAT Code")
    private String vatCode;


    private StockDetails stockDetails;
    


 
	public enum StockGroup implements EnumGetValue{
			Custom("CG","Custom"),
			Standard("SG","Standard");
		
		private String value;
		private String display;

		StockGroup(String value, String display) {
			this.value = value;
			this.display = display;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}
}
