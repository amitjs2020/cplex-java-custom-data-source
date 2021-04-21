
/*********************************************
 * OPL 12.5 Model
 * Author: jxu
 * Creation Date: Dec 24, 2015 at 9:42:13 AM
 *********************************************/

 
int scenarioRunId=...;
tuple CostData {
  int scenariorun_id;
  int  spendopti_ilog_supply_lineitem_id;
  int	supply_lineitem_id;
  int carrier_id;
  int ilog_demand_lineitem_id;
  float totalprice;
  float allocation;
}
{CostData} costData = ...;

tuple CarrierData {
  int carrierID;
}

{CarrierData} carrierData = ...;

tuple SupplyLineItemData {
  int supplyLineItemID;
}

{SupplyLineItemData} supplyLineItemData = ...;

tuple SupplyCarrierData {
  int supplyLineItem;
  int carrier;
}

{SupplyCarrierData} supplyCarrierData;  


{int} carrierIDSet;

{int} supplyLineItemIDSet;

tuple LaneinfoData {
  int scenariorun_id;
  int spendopti_ilog_demand_lineitem_id;
  int demand_lineitem_id;
  float estvolume;
  float penaltycost;
};
  
{LaneinfoData} laneinfoData = ...;

tuple CarrierLaneData {
  int carrier_id;
  int spendopti_ilog_demand_lineitem_id;
}
{CarrierLaneData} carrierLaneData=...;
  
{int} laneIDSet;

tuple GrouplaneData {
  int scenariorun_id;
  int ilog_group_id; 
  int ilog_demand_lineitem_id;
 }
 
{GrouplaneData} grouplaneData = ...;
 
tuple GroupData {
  int groupID;
}

{GroupData} groupData = ...;

tuple AmtGroupData {
  int aGroupID;
}
{AmtGroupData} aGrpData= ...;

 
{int} groupIDSet ={var1.groupID | var1 in groupData};

//{int} aGrpIDSet ={var1.aGroupID | var1 in aGrpData};

tuple GroupvolumeboundData {
  int group_id;
  int scenariorun_id;
  int carrier_id;
  float lowerbound_capacity;
  float upperbound_capacity;
}   

{GroupvolumeboundData} groupvolumeboundData = ...;

tuple GroupcarrierboundData {
  int group_id;
  int min_num;
  int max_num;
}  

{GroupcarrierboundData} groupcarrierboundData = ... ; 

tuple CarrierGroupData {
	int scenariorunid;
	int carrier_id;
	int group_id;
}	
{CarrierGroupData} carrierGroupData = ...;
	
/*tuple SupplyCarrierLane {
  int sli;
  int carrier;
  int lane;
}*/

//{SupplyCarrierLane} sclTriple = ...;
//SupplyCarrierLane aTriple;

tuple AmtGroupBoundData {
  int amtGroup_id;
  int scenariorun_id;
  int group_id;
  float amtLowerbound;
  float amtUpperbound;
} 
{AmtGroupBoundData} amtGroupBoundData = ...;

tuple AmtGroupCarrierData {
  int amtGroup_id;
  int scenariorun_id;
  int carrier_id;
}
{AmtGroupCarrierData} amtGroupCarrierData= ...;
      	
tuple ParameterData {
   int scenariorun_id;
   int global_min_carrier; 
   int global_max_carrier;
   int numberof_carrier_threshold;
   float admin_cost_bellow_threshold;
   float admin_cost_above_threshold;
   int mode;
   int result_parameter;
   int max_run_time;
}

{ParameterData} parameterData = ...;
    
int nLanes;
int nCarriers;
int nSupplyLineItems;
int nGroups;
int nAmtGroup;
int nTiers;
range Lanes=1..nLanes;
range Groups=1..nGroups;
range AmtGroups=1..nAmtGroup;
range Carriers=1..nCarriers;
range SupplyLineItems=1..nSupplyLineItems;
range Tiers=1..nTiers;
float bigM;
float smallM;


int r;
float f1;
float f2;
float maxAmt;
int mlower;
int mupper;
int mode;
int result_parameter;
int max_run_time;
float quan[1..nLanes];
float penaltyCost[Lanes];
tuple grpVolQuad{int i; int g; float volLower; float volUpper;};

tuple iCarrierLaneVolQuad{int ic; int lane; float volLower; float volUpper;};

tuple ICarrierLaneVolRawData{
int carrier_id; 
int spendopti_ilog_demand_lineitem_id; 
float min_volume; 
float max_volume;
};
{ICarrierLaneVolRawData} iCarrierLaneVolRawData = ...;
setof(iCarrierLaneVolQuad) iCarrierLaneVol;
iCarrierLaneVolQuad aQuad;

tuple ICarrierLaneRawData {
  int iCarrier_id;
  int spendopti_ilog_demand_lineitem_id;
}
{ICarrierLaneRawData} iCarrierLaneRawData=...;

tuple ICarrierLanePair {
  int iCarrier;
  int lane;
}

ICarrierLanePair iclPr;

setof(ICarrierLanePair) iCarrierLaneSet;

tuple NICarrierLaneRawData {
  int nICarrier_id;
  int ilog_demand_lineitem_id;
}

{NICarrierLaneRawData} nICarrierLaneRawData=...;
setof(int) nICarrierOnLaneSet[Lanes];

grpVolQuad aVolQuad;
setof(grpVolQuad) grpVolBound=...;
tuple grpNumTriple{int g; int numLower; int numUpper;};
grpNumTriple aNumTriple;
tuple amtGrpQuad{int amtGrpID; int groupID; float amtLower; float amtUpper;};
amtGrpQuad aAmtGrpQuad;

setof(grpNumTriple) grpNumBound=...;
setof(amtGrpQuad) amtGrpBound1=...;
setof(amtGrpQuad) amtGrpBound2=...;
tuple grpMoneyQuad{int i; int g; float moneyLower; float moneyUpper;};
grpMoneyQuad aMoneyQuad;
setof(grpMoneyQuad) grpMoneyBound=...;

tuple AssignPair{int sli; int lane;};
tuple AssignTriple{int sli; int carrier; int lane;};
AssignPair aPair;
AssignTriple anAssignTriple;
setof(AssignPair) assignPair=...;
setof(AssignTriple) assignTriple;
tuple CarrierLanePair{int carrier; int lane;};
CarrierLanePair clPair;
setof(CarrierLanePair) carrierLanePair=...;

tuple CarrierGroupPair{int carrier; int group;};
CarrierGroupPair cPair;
setof(CarrierGroupPair) cGrp=...;

float bidCost[assignPair];
tuple TierAllocQuad{int g; int t; float volLower; float volUpper;};
TierAllocQuad aTierQuad;
setof(TierAllocQuad) tierAllocBound=...;
tuple LaneGrpPair{int l; int g;};
LaneGrpPair aLGPair;
setof(LaneGrpPair) laneGrpPair=...;
tuple CarrierTierPair{int i; int t;};
CarrierTierPair aCTPair;

setof(CarrierTierPair) carrierTierPair=...;
{int} sameTierCarrier[Tiers];

setof(int) sameProportionGrp=...;
setof(int) sameProportionGrpData=...;
setof(int) sameCarrierGroup[Carriers];
grpVolQuad sameCGVolBound[cGrp];
grpMoneyQuad sameCGMoneyBound[cGrp];
tuple AllocationEntry{string info; int carrierID; int demandID; float price; float allocation;};
setof(AllocationEntry) allocationTable=...;
AllocationEntry anEntry;

tuple amtGroupCarrierPair{int aGrp; int carrier;};
amtGroupCarrierPair aGrpCarrierPair;
setof(amtGroupCarrierPair) amtGrpCarrier=...;

float calcCost;
tuple IndexPair{key int id1; int id2;};
IndexPair iPair;
IndexPair jPair;

//setof(int) sliList=...;
  

//{int} SliList;
//tuple MySet{ int first; int second;};
//tuple SameCarrierSLIData {SliSet;};
//int carrierSLILen[Carriers];
//{MySet} sameCarrierSet={<a,b> | a in Carriers, b in 1..carrierSLILen[a]};
{int} sameCarrierSLI[Carriers];
{int} sameLaneSLI[Lanes];
{int} sameSLILane[SupplyLineItems];
{int} sameGroupLane[Groups];
{int} sameGroupCarrier[Groups];
{int} sameAmtGroupCarrier[AmtGroups];
{int} sameCarrierLaneSLI[carrierLanePair];
{int} sameCarrierGroupLane[cGrp];
{int} sameCarrierLane[Carriers];
setof(AssignPair) sameCarrierAssignPair[Carriers];
setof(int) carrierVolSet;
setof(int) carrierMoneySet;
setof(int) sameCarrierVolGrp[Carriers];
setof(int) sameCarrierMoneyGrp[Carriers];
//int laneGroupPairTrue[laneGrpPair];



{AssignPair} grpVolAssignPair[grpVolBound];
{AssignPair} grpMoneyAssignPair[grpMoneyBound];
{AssignPair} tierAllocAssignPair[tierAllocBound];

//setof(int) myIntSet;
setof(IndexPair) carrierIndexTable=...;
setof(IndexPair) sliIndexTable=...; 
setof(IndexPair) laneIndexTable=...;
setof(IndexPair) groupIndexTable=...;
setof(IndexPair) amtGroupIndexTable=...;

setof(int) indexListCarrier=...;
setof(int) indexListLane=...;
setof(int) indexListGroup=...;
 
int numLanesInGroup[Groups];
 
dvar float+ x[assignPair]; //x_s_l
dvar float+ xbar[Lanes]; //xbar_i
dvar boolean ytilde[assignPair];  //ytilde_s_l
dvar int+ ytildeSum[carrierLanePair]; 
dvar boolean ybar[carrierLanePair];  //ybar_i_l
dvar boolean y[Carriers]; //y_s
dvar boolean u[cGrp];  //u_ig
dvar float+ z1;
dvar float+ z2;
dvar float+ xMoneySum[AmtGroups][Carriers];
dvar int+ zInt[assignPair];

  int i ;
   int j;
   int i1;
   int i2;
   int n1;
   int n2;
   int s;
   int s1;
   //var a,b;
int sli_idx;
int carrier_idx;
int lane_idx;

 
 execute PARAMS {
}
 
execute preprocessing1 {
 
   var timestamp;
   timestamp=Date();
   //read parameter 

   for (var myPara in parameterData) {
    mlower=myPara.global_min_carrier;
    mupper=myPara.global_max_carrier;
    r=myPara.numberof_carrier_threshold;
    f1=myPara.admin_cost_bellow_threshold;
    f2=myPara.admin_cost_above_threshold;   
    mode=myPara.mode;
    result_parameter=myPara.result_parameter;
    max_run_time=myPara.max_run_time;    		
   }   
   //read laneinfoData 

	i=0;
	for (var laneDataVar in laneinfoData) {
	  laneIDSet.add(laneDataVar.spendopti_ilog_demand_lineitem_id);
	  //iPair.id1=laneDataVar.spendopti_ilog_demand_lineitem_id;
	  //iPair.id2=i+1;
      //laneIndexTable.add(iPair.id1,iPair.id2);
      i=i+1;
      laneIndexTable.add(laneDataVar.spendopti_ilog_demand_lineitem_id, i)
      //i=i+1;
    }	  
	nLanes=i; 
	
	i=1;
	maxAmt=0;
	for (var laneDataVar1 in laneinfoData) {	
	  quan[i]=laneDataVar1.estvolume;
	  //penaltyCost[i]=laneDataVar1.penaltycost;
	  //if (penaltyCost[i]==0)
	  //  penaltyCost[i]=99999.99;
	  if (maxAmt<quan[i]) {
	    maxAmt=quan[i];
      }	    
	  i=i+1;
    }	  	
    if (maxAmt<nLanes) {
        maxAmt=nLanes+1;
    }          
	//build carrier table
	i=0;
	for (var carrierVar in carrierData) {
	      carrierIDSet.add(carrierVar.carrierID);
	      //iPair.id1=carrierVar.carrierID;
	  	  //iPair.id2=i+1;
          i=i+1;
          //carrierIndexTable.add(iPair.id1,iPair.id2);
          carrierIndexTable.add(carrierVar.carrierID,i);          
    }          
    nCarriers=i;
    
    //build supplyLineItem table
    i=0;
    for (var sliVar in supplyLineItemData) {
	      supplyLineItemIDSet.add(sliVar.supplyLineItemID);
	      //iPair.id1=sliVar.supplyLineItemID;
	  	  //iPair.id2=i+1;
          //sliIndexTable.add(iPair.id1,iPair.id2);
          i=i+1;
          sliIndexTable.add(sliVar.supplyLineItemID,i);
          
    }          
    nSupplyLineItems=i;
    
     for (var var2 in costData ) {  

	    sli_idx=sliIndexTable.get(var2.spendopti_ilog_supply_lineitem_id).id2;

	    carrier_idx=carrierIndexTable.get(var2.carrier_id).id2;
        
		//iPair.id1=sli_idx;
		//iPair.id2=carrier_idx;
		//supplyCarrierData.add(iPair);
		supplyCarrierData.add(sli_idx,carrier_idx);

		//lane_idx=laneIndexTable.get(i2).id2;
		lane_idx=laneIndexTable.get(var2.ilog_demand_lineitem_id).id2;
	    //aPair.sli=sli_idx;
	    //aPair.lane=lane_idx;

      	//assignPair.add(aPair);
      	assignPair.add(sli_idx, lane_idx);
      	//anAssignTriple.sli=sli_idx;
      	//anAssignTriple.carrier=carrier_idx;
      	//anAssignTriple.lane=lane_idx;
      	//assignTriple.add(anAssignTriple);
      	assignTriple.add(sli_idx,carrier_idx, lane_idx);

     }
     var timestamp1;
     timestamp1=Date();
     writeln("time for 1st block: ",timestamp1-timestamp);
     
    // writeln("before=",carrierLanePair);   
 	for (var var0 in carrierLaneData) {
 	  i1=var0.carrier_id;
 	  i2=var0.spendopti_ilog_demand_lineitem_id;//ilog_demand_lineitem_id;
 	  carrier_idx=carrierIndexTable.get(i1).id2;
 	  lane_idx=laneIndexTable.get(i2).id2;
 		jPair.id1=carrier_idx;
		jPair.id2=lane_idx;
      	carrierLanePair.add(jPair);
      	sameCarrierLane[carrier_idx].add(lane_idx);
     } 
     for (var var0 in iCarrierLaneRawData) {
 	   i1=var0.iCarrier_id;
 	   i2=var0.spendopti_ilog_demand_lineitem_id;//ilog_demand_lineitem_id;
 	   carrier_idx=carrierIndexTable.get(i1).id2;
 	   lane_idx=laneIndexTable.get(i2).id2;
 	   iclPr.iCarrier=carrier_idx;
 	   iclPr.lane=lane_idx; 	   
	   iCarrierLaneSet.add(iclPr);
	}	
	  
     for (var var0 in nICarrierLaneRawData) {
 	  i1=var0.nICarrier_id;
 	  i2=var0.ilog_demand_lineitem_id;//ilog_demand_lineitem_id;
 	  carrier_idx=carrierIndexTable.get(i1).id2;
 	  lane_idx=laneIndexTable.get(i2).id2;
 	  
	  nICarrierOnLaneSet[lane_idx].add(carrier_idx);
	}		
     
	for (var var0 in iCarrierLaneVolRawData) {
 	  i1=var0.carrier_id;
 	  i2=var0.spendopti_ilog_demand_lineitem_id;//ilog_demand_lineitem_id;
 	  carrier_idx=carrierIndexTable.get(i1).id2;
 	  lane_idx=laneIndexTable.get(i2).id2;
       aQuad.ic=carrier_idx;
	  aQuad.lane=lane_idx;
       aQuad.volLower=var0.min_volume;
	  aQuad.volUpper=var0.max_volume;
       iCarrierLaneVol.add(aQuad);
	}
	   
		
//the following function is rewritten for performance
/*    for (var var0 in carrierLanePair) {
     i1=var0.carrier; //carrier
     i2= var0.lane; //lane
     for (var var1 in assignTriple) {
       if (var1.lane == i2 && var1.carrier==i1) {
         sameCarrierLaneSLI[var0].add(var1.sli);
       }
     }                
    } */  
 	for (var var1 in assignTriple) {
 	  clPair.carrier=var1.carrier;
 	  clPair.lane=var1.lane;
 	  sameCarrierLaneSLI[clPair].add(var1.sli);
 	  sameCarrierAssignPair[var1.carrier].add(var1.sli,var1.lane);
    } 	  
    
    for (var var0 in supplyCarrierData) {
      i=var0.carrier;
      //n=carrierSLILen[i];
      //n=n+1;
      //carrierSLILen[i]=n;
	  //sameCarrierSet.add(i,n);
      sameCarrierSLI[i].add(var0.supplyLineItem);	  	
    } 
    
 
    if (maxAmt<nSupplyLineItems) {
      maxAmt=nSupplyLineItems+1;
    }    
    if (bigM<maxAmt) {
      bigM=maxAmt;
    }
                    
    for (var var0 in assignPair) {
      i=var0.lane;
      sameLaneSLI[i].add(var0.sli);
      //sameSLILane[var0.sli].add(i);
    }            
 
        
     calcCost=0;
     //i=0;
     for (var2 in costData ) {  
      	//i1=var2.spendopti_ilog_supply_lineitem_id;
      	//i2=var2.ilog_demand_lineitem_id;  //database column name misleading

		//aPair.sli=sliIndexTable.get(i1).id2;

        //aPair.lane=laneIndexTable.get(i2).id2;              	      	
      	//bidCost[aPair]=var2.totalprice;
      	
      	i1=sliIndexTable.get(var2.spendopti_ilog_supply_lineitem_id).id2;
      	i2=laneIndexTable.get(var2.ilog_demand_lineitem_id).id2;
      	aPair.lane=i2;
      	aPair.sli=i1;
      	bidCost[aPair]=var2.totalprice;
      	calcCost=calcCost+var2.totalprice;
      	//i=i+1;      	
     }
     if ( nSupplyLineItems>0 && calcCost>0) {
       calcCost=1000*calcCost/nSupplyLineItems;
     }
     else {
       calcCost=9999.99;
     }              
	i=1;
	for (var laneDataVar1 in laneinfoData) {	
	  penaltyCost[i]=laneDataVar1.penaltycost;
	  if (penaltyCost[i]==0)
	    penaltyCost[i]=calcCost;
	  i=i+1;
    }	  		
	
	//read groupData
 	i=0;
	for (var groupVar in groupData) {
	      groupIDSet.add(groupVar.groupID);
	      //iPair.id1=groupVar.groupID;
	  	  //iPair.id2=i+1;
         // groupIndexTable.add(iPair); 
          i=i+1;
          groupIndexTable.add(groupVar.groupID,i);
    }          
    nGroups=i;    

 	i=0;
	for (var aGrpVar in aGrpData) {
	      //groupIDSet.add(groupVar.groupID);
	      //iPair.id1=groupVar.groupID;
	  	  //iPair.id2=i+1;
         // groupIndexTable.add(iPair); 
          i=i+1;
          amtGroupIndexTable.add(aGrpVar.aGroupID,i);
    }          
    nAmtGroup=i;   
        
   //read grouplaneData table
      
     for (var var4 in grouplaneData ) {
      	i1=var4.ilog_group_id;
      	i2=var4.ilog_demand_lineitem_id;
      	if (i2==0) {
      		
        }
        else {
	      	aLGPair.g=groupIndexTable.get(i1).id2;

	      	aLGPair.l=laneIndexTable.get(i2).id2;

      		laneGrpPair.add(aLGPair);
      		//laneGroupPairTrue[aLGPair]=1;
      		sameGroupLane[aLGPair.g].add(aLGPair.l);          
        }                		
      	
   	 }

	var ts2;
	ts2=Date();
	writeln("time for 2nd block: ", ts2-timestamp1);
	
	// read carrierGroupData
	for (var var4 in carrierGroupData) {
	    i2=var4.group_id;
      	i1=var4.carrier_id;
	  
	      	cPair.group=groupIndexTable.get(i2).id2;
	      	cPair.carrier=carrierIndexTable.get(i1).id2;
      	cGrp.add(cPair);
    }      	
	var ts3;
	ts3=Date();
	writeln("time for 3rd block: ", ts3-ts2);
	
   for (var var1 in cGrp) {
     sameGroupCarrier[var1.group].add(var1.carrier);
     sameCarrierGroup[var1.carrier].add(var1.group);
   }

/*   for (var var1 in cGrp) {        
     for (var l in sameCarrierLane[var1.carrier]) {
       aLGPair.l=l;
       aLGPair.g=var1.group;
         if (laneGrpPair.contains(aLGPair ) ) {  
  	       sameCarrierGroupLane[var1].add(l);
          }  	       
      }        	     
   }  */
   
 for (var var1 in laneGrpPair) {        
     for (var c in Carriers) {
       if (sameCarrierLane[c].contains(var1.l ) ) {
         cPair.carrier=c;
         cPair.group=var1.g;
         if (cGrp.contains(cPair)) {  
  	       sameCarrierGroupLane[cPair].add(var1.l);
          }  	       
        }          
      }        	     
   }  
   	var ts4;
	ts4=Date();
	writeln("time for 4th block: ", ts4-ts3);
   //read tier Carrier Pair Data .. to be complete

   for (var var1 in carrierTierPair) {
	sameTierCarrier[var1.t].add(var1.i);
  }	     
   
   //read group volume bound
   //group_id	scenariorun_id	carrier_id	lowerbound_capacity	upperbound_capacity
    
     for (var var5 in groupvolumeboundData) {
      	i1=var5.group_id;
      	i2=var5.carrier_id;

	      	aVolQuad.i=carrierIndexTable.get(i2).id2;
        
	      	aVolQuad.g=groupIndexTable.get(i1).id2;
 
      	aVolQuad.volLower=var5.lowerbound_capacity;
      	aVolQuad.volUpper=var5.upperbound_capacity;
      	grpVolBound.add(aVolQuad);
   	 }
   	var ts5;
	ts5=Date();
	writeln("time for 5th block: ", ts5-ts4);
 /*  for (var var5 in grpVolBound) {
    for (s in sameCarrierSLI[var5.i]) {
      for (l in sameGroupLane[var5.g]) {
        //for (var var6 in assignPair) {
          //if (var6.sli==s && var6.lane==l) {
        aPair.sli=s;
        aPair.lane=l;    
        if (assignPair.contains(aPair) ) {    
            grpVolAssignPair[var5].add(aPair);
          }            
        //}
      }
    }                         
   }  */
  for (var var5 in grpVolBound) {
    carrierVolSet.add(var5.i);
    sameCarrierVolGrp[var5.i].add(var5.g);
    cPair.group=var5.g;
    cPair.carrier=var5.i;
    //writeln("grpVolBound", var5, "-",cPair);
    sameCGVolBound[cPair]=var5;

  }    
   
/*   for (var var5 in grpVolBound) {
    
 	for (var var6 in sameCarrierAssignPair[var5.i]) {
   
        if (sameCarrierSLI[var5.i].contains(var6.sli) && sameGroupLane[var5.g].contains(var6.lane)) {
          //cPair.group=var7;
          //cPair.carrier=var5;    
          grpVolAssignPair[var5].add(var6);
        }            
    }                         
   }              
*/
  for (var var5 in carrierVolSet) {
    
 	for (var var6 in sameCarrierAssignPair[var5]) {
 	  for (var var7 in sameCarrierVolGrp[var5]) {   
        if (sameGroupLane[var7].contains(var6.lane)) {
          cPair.group=var7;
          cPair.carrier=var5;    
          //write("new ..", var5, var7);
          grpVolAssignPair[sameCGVolBound[cPair]].add(var6);
          //writeln("2..",var5, var7);
        }            
      }
    }                         
   }     
       
  	var ts6;
	ts6=Date();
	writeln("time for 6th block: ", ts6-ts5);

   //read group money bound 

  for (var var5 in grpMoneyBound) {
    carrierMoneySet.add(var5.i);
    sameCarrierMoneyGrp[var5.i].add(var5.g);
    cPair.group=var5.g;
    cPair.carrier=var5.i;
    //writeln("grpVolBound", var5, "-",cPair);
    sameCGMoneyBound[cPair]=var5;

  }    

   
  for (var var5 in carrierMoneySet) {   
 	for (var var6 in sameCarrierAssignPair[var5]) {
 	  for (var var7 in sameCarrierMoneyGrp[var5]) {   
        if (sameGroupLane[var7].contains(var6.lane)) {
          cPair.group=var7;
          cPair.carrier=var5;    
          grpMoneyAssignPair[sameCGMoneyBound[cPair]].add(var6);
        }            
      }
    }                         
   } 
   
   	var ts7;
	ts7=Date();
	writeln("time for 7th block: ", ts7-ts6);   
   //read tier allocation bound  .. to be completed


  
  for (var var5 in tierAllocBound) {
     for (i in sameTierCarrier[var5.t]) {
 		for (var var6 in sameCarrierAssignPair[i]) {
 	      if (sameGroupLane[var5.g].contains(var6.lane)) { 
            		tierAllocAssignPair[var5].add(aPair);           		            
        }
      }
    }                         
   }     
     	var ts8;
	ts8=Date();
	writeln("time for 8th block: ", ts8-ts7);
   //read group carrier bound

     for (var var6 in groupcarrierboundData) {
      	i1=var6.group_id;
      	i2=var6.min_num;
       	n1=var6.max_num;  
	    aNumTriple.g=groupIndexTable.get(i1).id2;
      	aNumTriple.numLower=i2;
      	aNumTriple.numUpper=n1;
      	
      	grpNumBound.add(aNumTriple);
   	 };
   	 
	var minAmt;
	var maxAmt;
   	 
     for (var var6 in amtGroupBoundData) {
      	i1=var6.amtGroup_id;
      	i2=var6.group_id;
      	minAmt=var6.amtLowerbound;
       	maxAmt=var6.amtUpperbound;  
	    aAmtGrpQuad.amtGrpID=amtGroupIndexTable.get(i1).id2;
	    aAmtGrpQuad.groupID=groupIndexTable.get(i2).id2;
      	aAmtGrpQuad.amtLower=minAmt;
      	aAmtGrpQuad.amtUpper=maxAmt;      	
	    if (sameGroupLane[aAmtGrpQuad.groupID].size > 0 ) {
	      	amtGrpBound1.add(aAmtGrpQuad);
        }
        else {	      	
	      	amtGrpBound2.add(aAmtGrpQuad);
        }
   	 }; 
   	 
   	 for (var var7 in amtGroupCarrierData) {
   	   i1=var7.amtGroup_id;
   	   i2=var7.carrier_id;
   	   aGrpCarrierPair.aGrp=amtGroupIndexTable.get(i1).id2;
   	   aGrpCarrierPair.carrier=carrierIndexTable.get(i2).id2;
   	   amtGrpCarrier.add(aGrpCarrierPair);
   	   sameAmtGroupCarrier[aGrpCarrierPair.aGrp].add(aGrpCarrierPair.carrier);
   	   writeln(aGrpCarrierPair.aGrp,sameAmtGroupCarrier[aGrpCarrierPair.aGrp]);
     }   	   
   	     	 
    var ts9;
	ts9=Date();
	writeln("time for 9th block: ", ts9-ts8);
   //build carrier group

   
  
 //  for ( var var7 in supplyCarrierData ) {
 //    for ( var var8 in assignPair ) {
//	   for (var var9 in laneGrpPair) {
//	     if (var7.supplyLineItem == var8.sli and var8.lane == var9.lane) {
//	        	cPair.carrier=var7.carrier;
//         		cPair.group=g;
//         		cGrp.add(cPair);
 //        }         		
  //     }                  		
 //      }         		
 //    }                    		         	         		         

    
    //build number of lanes in each group
    for (i in Groups) {
      numLanesInGroup[i]=0;
    };
    
    for (var a in laneGrpPair) {
      numLanesInGroup[a.g]= numLanesInGroup[a.g]+1;
    } ;   
    
    //build carrier leveling proportion rule
    for (var gID in sameProportionGrpData) {
	      	sameProportionGrp.add( groupIndexTable.get(gID).id2);
    }; 
    //supplyCarrierData.end();
    writeln(" time for preprocessing ", Date() - ts9);     
    
  //cplex.epgap = 1e-3;
  if (result_parameter ==2 && max_run_time >0) {
    cplex.tilim=max_run_time;
    
  } 
  
  if (result_parameter ==1 || result_parameter ==0 ) {
    smallM=0.01;   
  }
  else {
    smallM=1;
  }        
  /* writeln("A ",amtGrpBound1);
   writeln("B ",sameAmtGroupCarrier);
   //writeln(sameGroupLane);
   writeln("C ",sameCarrierLaneSLI);
   //writeln(assignPair);
   //for (var1 in amtGrpBound1) {
   //   writeln("var1 ",var1);
   //   for (c in sameAmtGroupCarrier[var1.amtGrpID]) {
  //         writeln("c ",c);
   //        for (l in sameGroupLane[var1.groupID]) {
   //          writeln("l ",l);
              clPair.carrier=c;
              clPair.lane=l;
             writeln("sli ",sameCarrierLaneSLI[clPair]);
             for (s in sameCarrierLaneSLI[clPair]) {
                 writeln("sli ",s);
                 aPair.sli=s;
                 aPair.lane=l;
                 writeln("assign ",assignPair[aPair]);
               }
             }
         }                                                        
          //sum( l in sameGroupLane[var1.groupID],s in sameCarrierLaneSLI[<c,l>], <s,l> in assignPair) bidCost[<s,l>]*x[<s,l>] <= var1.amtUpper;
      
    }         
    */      
};
    
 
  minimize sum ( var in assignPair )bidCost[var] * x[var] 
 + sum ( l in Lanes) penaltyCost[l]*xbar[l] +f1*z1+f2*z2 ;
 //+ sum ( i in Carriers ) 0.1*y[i]
 //+ sum( var1 in assignPair )0.1*ytilde[var1] ;

 //constraint ct2[Lanes];
 //constraint ct3a1[grpVolBound];
 //constraint ct3a2[grpVolBound];
 //constraint ct3b1[grpMoneyBound];
 //constraint ct3b2[grpMoneyBound];
 //constraint ct3c1[tierAllocBound];
 //constraint ct3c2[tierAllocBound];
 //constraint ct4a1[cGrp];
 //constraint ct4a2[cGrp];
 //constraint ct4b1[Groups];
 //constraint ct4b2[Groups];
 //constraint ct6[cGrp];
//constraint ct7[Carriers][Lanes][Lanes];
 //constraint ct8a[SupplyLineItems][Lanes];
 //constraint ct8b[SupplyLineItems][Lanes];
 //constraint ct9a[assignPair];
 //constraint ct9b[assignPair];
 //constraint ct10[SupplyLineItems];
 //constraint ct11a1[carrierLanePair];
//constraint ct11a2[carrierLanePair];
 
 //constraint ct11b[carrierLanePair];
 //constraint ct11c[Carriers];
 //constraint ct11d[Carriers];
 //constraint c12;
 //constraint c14[amtGrpBound];
 
 //constraints
  subject to {
    //ct1a1: 
	sum ( i in Carriers ) y[i] <= mupper ;
    //ct1a2: 
	sum ( i in Carriers )  y[i] >= mlower ;  //constraint 1-1
    //ct1b: 
	z1 + z2 - sum(i in Carriers) y[i] == 0 ;  //constraint 1-b
    //ct1c: 
	z1 <= r ; //constraint 1-c
    
    forall (l in Lanes){ 
      //ct2[l]: 
	sum(s in sameLaneSLI[l])x[<s,l>] + xbar[l] == quan[l]; // constraint 2 
		//ct2[l]: sum(s in sameLaneSLI[l])x[<s,l>]  == quan[l]; // constraint 2
	}		 
  	//x[<1,1>] == 0;
    //constraint 3a  
    forall (var1 in grpVolBound) {   
      //ct3a1[var1]: 
	sum(var2 in grpVolAssignPair[var1]) x[var2] <= var1.volUpper ;    
      //ct3a2[var1]: 
	sum( var2 in grpVolAssignPair[var1]) x[var2] >= var1.volLower ;    

      //ct3a1[var1]: sum (var2 in assignPair, var2.lane in sameGroupLane[var1.g], var2.sli in sameCarrierSLI[var1.i]) x[var2] <= var1.volUpper ;
      //ct3a2[var1]: sum (l in sameGroupLane[var1.g], s in sameCarrierSLI[var1.i], <s,l> in assignPair)x[<s,l>] >= var1.volLower ;
  } 	  
    
   //constraint 3b 
    forall (var1 in grpMoneyBound) {
      //ct3b1[var1]: 
	sum( p in grpMoneyAssignPair[var1]) bidCost[p]*x[p] <= var1.moneyUpper ;
      //ct3b2[var1]: 
	sum( p in grpMoneyAssignPair[var1]) bidCost[p]*x[p] >= var1.moneyLower ;
  	};    
    
    //constraint 3c
    forall (var1 in tierAllocBound) {
      //ct3c1[var1]: 
	sum (p in tierAllocAssignPair[var1] ) x[p] <= var1.volUpper ;
      //ct3c2[var1]: 
	sum (p in tierAllocAssignPair[var1] ) x[p] >= var1.volLower ;

//      ct3c1[var1]: sum (l in Lanes: <l,var1.g> in laneGrpPair, i in Carriers: <i,var1.t> in carrierTierPair, s in SupplyLineItems: <s,i> in  supplyCarrierData ) x[<s,l>] <= var1.volUpper ;
//      ct3c2[var1]: sum (l in Lanes: <l,var1.g> in laneGrpPair, i in Carriers: <i,var1.t> in carrierTierPair, s in SupplyLineItems: <s,i> in  supplyCarrierData ) x[<s,l>] >= var1.volLower ;

  	};    
        
   //constraint 4a
  	forall (var1 in cGrp) {
  	  //ct4a1[var1]: 
	card(sameCarrierGroupLane[var1])*u[var1] - sum(l in sameCarrierGroupLane[var1]) ybar[<var1.carrier,l>] >= 0 ;  
  	  //ct4a2[var1]: 
	u[var1] - sum( l in sameCarrierGroupLane[var1]) ybar[<var1.carrier,l>] <= 0 ;
   } 	    
      
    //constraint 4b  
    forall (var1 in grpNumBound) {
      //ct4b1[var1.g]: 
	sum(i in sameGroupCarrier[var1.g]) u[<i,var1.g>] <= var1.numUpper ;        
       //ct4b1[var1.g]: sum(i in sameGroupCarrier[var1.g]) u[<i,var1.g>] <= card(sameGroupCarrier[var1.g]) ;        
 
      //ct4b2[var1.g]: 
	sum(i in sameGroupCarrier[var1.g]) u[<i,var1.g>] >= var1.numLower ;              
    };             
    
    //constraint 5 is not to implement at this phase
    
    //constraint 6
  //  forall (var1 in cGrp) {// not  cGrp, should be all business based on parameter
  //    ct6[var1]: sum( l in sameCarrier GroupLane[var1]) ybar[<var1.carrier,l>] - numLanesInGroup[var1.group]*u[var1] == 0 ; 
  //  } 
    
    //constraint 7
/*    forall (g in sameProportionGrp) { 
      forall (<i,g> in cGrp, <l1,g> in laneGrpPair, <l2,g> in laneGrpPair: l2>l1) {
            ct7[i][l1][l2]: sum( s in sameLaneSLI[l1]: <s,i> in  supplyCarrierData ) x[<s,l1>]/quan[l1] - sum( s in sameLaneSLI[l2]: <s,i> in  supplyCarrierData )x[<s,l2>]/quan[l2] == 0;
       
      }
    }                                    
  */
 
   
    
    forall (g in sameProportionGrp) { 
      forall (i in sameGroupCarrier[g]) {
        forall (l1 in sameGroupLane[g]) {
          forall (l2 in sameGroupLane[g]) {
            if (l2>l1) {
            	//ct7[i][l1][l2]: 
            	 sum( s in sameLaneSLI[l1]: <s,i> in  supplyCarrierData ) x[<s,l1>]/quan[l1] - sum( s in sameLaneSLI[l2]: <s,i> in  supplyCarrierData )x[<s,l2>]/quan[l2] == 0 ;
            	 //sum( s in sameLaneSLI[l1]: <s,i> in  supplyCarrierData ) x[<s,l1>]/quan[l1] - sum( s in sameLaneSLI[l2]: <s,i> in  supplyCarrierData )x[<s,l2>]/quan[l2] == 0;
            
            }
          }            
        }                   
      }
    }                                    
      
    //constraint 8 
    /*forall (s in SupplyLineItems) {
      forall (l in Lanes) {
        if (<s,l> not in assignPair) {
          ct8a[s][l]: x[s][l]  == 0 ;
          ct8b[s][l]: ytilde[s][l] == 0 ;
        }
      }
    }                        
     */
    //constraint 9 
    forall (var in assignPair) {
      //ct9a[var]: 
	x[var]-bigM*ytilde[var] <= 0 ;
      //ct9b[var]: 
	x[var] - smallM*ytilde[var] >= 0 ;
 
    }                    
    
 //   constraint 10 (modified for model v4)each supplyline item can only be allocated to one demand lane
    forall (s in SupplyLineItems) {
      //ct10[s]: 
	sum ( l in sameSLILane[s]: <s,l> in assignPair) ytilde[<s,l>]  <= 1 ;
    }      
 
    //constraint 11 (for model v4)
    forall ( var1 in carrierLanePair) {
      
      //sliSet=sameCarrierSLI[var1.carrier];    
      	//ct11a1[var1]: 
		sum ( s in sameCarrierLaneSLI[var1]) ytilde[<s,var1.lane>] -ytildeSum[var1]  == 0 ;
   		//ct11a2[var1]:  
		ytildeSum[var1] -bigM*ybar[var1]  <=0 ;
   		//ct11b[var1]: 
		ytildeSum[var1] -ybar[var1]  >=0 ;
     }
   
 
 //  forall (i in Carriers) {
 //     forall (l in Lanes) {
 //     }
 //  }    
   forall (i in Carriers) {
     //ct11c[i]: 
	sum ( l in sameCarrierLane[i]) ybar[<i,l>] - bigM*y[i] <=0 ;    		
     //ct11d[i]: 
	sum ( l in sameCarrierLane[i]) ybar[<i,l>] - y[i] >=0 ;    		
   }
   
//ct12
   forall (var6 in iCarrierLaneVol) {
	//ct12a[i][l]
	sum(s in sameCarrierSLI[var6.ic] : <s,var6.lane> in assignPair ) x[<s,var6.lane>] >= var6.volLower; 
	//ct12b[i][l]
	sum(s in sameCarrierSLI[var6.ic] : <s, var6.lane> in assignPair ) x[<s,var6.lane>] <= var6.volUpper; 
  }

  //ct13
   forall (var1 in iCarrierLaneSet) {
     forall (i in nICarrierOnLaneSet[var1.lane]) {
       ybar[<var1.iCarrier,var1.lane>] - ybar[<i,var1.lane>] >= 0 ;
     }
   } 	 

  //ct14
  forall (var1 in amtGrpBound1) {
    
      forall (c in sameAmtGroupCarrier[var1.amtGrpID]) {
//          sum( l in sameGroupLane[var1.groupID],s in sameCarrierLaneSLI[<c,l>]: <s,l> in assignPair) bidCost[<s,l>]*x[<s,l>] >= var1.amtLower;
//          sum( l in sameGroupLane[var1.groupID],s in sameCarrierLaneSLI[<c,l>]: <s,l> in assignPair) bidCost[<s,l>]*x[<s,l>] <= var1.amtUpper;
          sum( p in sameCarrierAssignPair[c]) bidCost[p]*x[p] >= var1.amtLower;
          sum( p in sameCarrierAssignPair[c]) bidCost[p]*x[p] <= var1.amtUpper;
        
      } 
                                	
  }
  
  forall (var1 in amtGrpBound2) {

        forall (c in sameAmtGroupCarrier[var1.amtGrpID]) {
    	    sum( var2 in sameCarrierAssignPair[c] ) bidCost[var2]*x[var2] - xMoneySum[var1.amtGrpID][c] == 0;
    	    xMoneySum[var1.amtGrpID][c] >= var1.amtLower;
    	    xMoneySum[var1.amtGrpID][c] <= var1.amtUpper;
        }    	    
  }      
  //ct15
  if (mode==3 && result_parameter==2) {
    forall (var1 in assignPair) {
      x[var1] - zInt[var1] == 0;
    }
  }          
   
}; 
  //int i1;
  //int j1;


//Define the model oputput data
tuple ModeloutputData {
  float allocation;
//  int scenariorun_id;
  int spendopti_ilog_supply_lineitem_id;
//  int supply_lineitem_id;
//  int carrier_id;
//  int ilog_demand_lineitem_id;
//  float totalprice;

}

{ModeloutputData} modeloutputData; 
ModeloutputData anOut;

tuple DeficitoutputData {
  int scenario_id;
  int ilog_demand_lineitem_id;
  float Allocation;
}

{DeficitoutputData} deficitoutputData;
DeficitoutputData bOut;
  
//postprocessing to write the model output back to the database
  execute postprocessing1 {

/*	supplyCarrierData.end();
	laneinfoData.end();
	carrierLaneData.end();
	grouplaneData.end();
	groupvolumeboundData.end();
	groupcarrierboundData.end();
	carrierGroupData.end();
	carrierLanePair.end();

	tierAllocBound.end();
	laneGrpPair.end();
	*/
	//stop here
	//carrierTierPair.end();
/*	sameTierCarrier.end();
	sameProportionGrp.end();
    sameProportionGrpData.end();
    sameCarrierGroup.end();
    sameCGVolBound.end();
         
    sameCGMoneyBound.end();
 	sameCarrierSLI.end();
	sameLaneSLI.end();
	sameSLILane.end();
	sameGroupLane.end();
	sameGroupCarrier.end();
	sameCarrierLaneSLI.end();
	sameCarrierGroupLane.end();
	sameCarrierLane.end();

	carrierVolSet.end();
	carrierMoneySet.end();
	sameCarrierVolGrp.end();
	sameCarrierMoneyGrp.end();  
	grpVolAssignPair.end();
	grpMoneyAssignPair.end();
	tierAllocAssignPair.end();	*/
	//for (var v in Carriers) {
	//  sameCarrierAssignPair[v].end();  
    //}	  
	//sameCarrierAssignPair.end();


/*	bidCost.end();
	cGrp.end(); 
	assignPair.end();
	assignTriple.end();
	
*/
		
	//thisOplModel.endAll();
	
    writeln("start post");
 	for (var v in costData) {
 	  
 	  //anOut.scenariorun_id=v.scenariorun_id;
 	  anOut.spendopti_ilog_supply_lineitem_id=v.spendopti_ilog_supply_lineitem_id;
 	  //anOut.supply_lineitem_id=v.supply_lineitem_id;
 	  /*for (var var1 in sliIndexTable) {
		  if (var1.id1==v.spendopti_ilog_supply_lineitem_id) {		  
	 		i1=var1.id2;
	      	break;
          }	      	
      }	
      */
      i1=sliIndexTable.get(v.spendopti_ilog_supply_lineitem_id).id2;   

 		j=laneIndexTable.get(v.ilog_demand_lineitem_id).id2;	    
  //    anOut.ilog_demand_lineitem_id=v.ilog_demand_lineitem_id;
  //    anOut.totalprice=v.totalprice;
      aPair.sli=i1;
      aPair.lane=j;
      anOut.allocation=x[aPair];
      //writeln(anOut);              	         			  
	  modeloutputData.add(anOut);	
	  //writeln(aPair.sli," "+aPair.lane," "+x[aPair]);
    } 
    writeln("End updating");
    for (var varLane in laneIDSet) {
	  //j=-1;	
 	  /*for (var var5 in laneIndexTable) {
		  if (var5.id1==varLane) {		  
	      	j1=var5.id2;
	      	break;
          }	      	
      }	*/
      j=laneIndexTable.get(varLane).id2;     
      if (j>=0) {
        if (xbar[j]>0) {
          bOut.scenario_id=scenarioRunId;
          bOut.Allocation=xbar[j];
  		  bOut.ilog_demand_lineitem_id=varLane;
  		  deficitoutputData.add(bOut);
        }          		  
      }
   }    	  		  
   //costData.end();
   //cplex.end();
   writeln("the end"); 	   
  };    
 