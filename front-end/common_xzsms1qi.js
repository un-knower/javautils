//******************************************************************************************************
// Program ID   : common.js
// Purpose      : Common function in prc_bo
// Author       : ChenHongRi
// Date Written : 2001-09-07 15:36:20
// Description  : Put your reference here
// Change Log   : Put your maintenance history here
//******************************************************************************************************
var  m_commonMsg00000 = "无效的日期格式(日期格式为：YYYY/MM/DD)";   //  top.hide.g_strInvalidDateFormat;
var  isAlert = false;//是否已经弹出过操作信息 Milkli 2004-4-7日增加
var winMessage;
/******************************************************************************************************
Function	: reportPrint()
Purpose		: print report for current report
Parameter	: void
Return		: void
First time  : 2001-12-05
Author      : ChenHongRi
******************************************************************************************************/
function reportPrint()
{
    window.focus();
	window.print();
}
/******************************************************************************************************
Function	: formatNumber(iNum, iDn)
Purpose		: Format number
Parameter	: String iNum,iDn
Return		: String
First time  : 2001-08-29
Author      : ChenHongRi
******************************************************************************************************/
function formatNumber(iNum, iDn)
{
	var iTmp="";
	if ( iDn == null )
		iDn = 2;
	var strNum =iNum + "";
	strNum =  trim(strNum);
	if (isNaN(strNum) || strNum.length == 0)
	{
		for (i = 0; i < iDn; i++)
			iTmp += "0"
		return "0."+iTmp;
	}

   	var ifirst=0;
	for(var iN=0;iN<strNum.length-1;iN++)
	{
		if(strNum.indexOf(".")==1)
			break;
		else
		{
			if(strNum.charAt(iN)=="0") ifirst+=1;
			else break;
		}
	}
	strNum=strNum.substring(ifirst);
	if(strNum.indexOf(".")==0) strNum="0"+strNum;
	if(strNum.indexOf("-.")==0) strNum="-0."+strNum.substring(2);
	var iDi = strNum.indexOf(".",0);
	if(iDi < 0)
	{
		if (iDn==0) return strNum;     //  iDn==0 不保留小数位
		for (i = 0; i < iDn; i++)
			iTmp += "0"
		strNum  += "."+iTmp;
		return strNum;
	}
	var iDiLength = strNum.length - (iDi+1);
	if (iDiLength == iDn)
		return strNum;
	if (iDiLength < iDn)
	{
		for (var iLoop=iDn; iLoop>iDiLength; iLoop--)
		{
			strNum += '0';
		}
		return strNum;
	}
	else
	{
		var iNum1 = parseFloat(strNum);
		iNum1 = (iNum1+(5/Math.pow(10,(iDn+1))))*(Math.pow (10,iDn));
		iNum1 = Math.floor(iNum1);
		iNum1 = iNum1/Math.pow (10,iDn);
		strNum = new String(iNum1);
		if (iDn==0) return strNum;     //  iDn==0 不保留小数位
		var iDi = strNum.indexOf(".",0);
		if(iDi < 0)
		{
			for (i = 0; i < iDn; i++)
				iTmp += "0"
			strNum  += "."+iTmp;
			return strNum;
		}
		var iDiLength = strNum.length - (iDi+1);
		if (iDiLength == iDn){
			return strNum;}
		if (iDiLength < iDn)
		{
			for (var iLoop=iDn; iLoop>iDiLength; iLoop--)
			{
				strNum += '0';
			}
			return strNum;
		}
	}
}
/******************************************************************************************************
Function	: trim(strInput)
Purpose		:
Parameter	: String strInput
Return		: String
First time  : 2001-08-29
Author      : ChenHongRi
******************************************************************************************************/
function  trim(strInput)
{
	var iLoop  = 0;
	var iLoop2 = -1;
	var strChr;
	if((strInput == null)||(strInput == "<NULL>")) return "";
	if(strInput)
	{
		for(iLoop=0;iLoop<strInput.length-1;iLoop++)
		{
			strChr=strInput.charAt(iLoop);
			if(strChr!=' ')
				break;
		}
		for(iLoop2=strInput.length-1;iLoop2>=0;iLoop2--)
		{
			strChr=strInput.charAt(iLoop2);
			if(strChr!=' ')
				break;
		}
	}

	if(iLoop<=iLoop2)
	{
		return strInput.substring(iLoop,iLoop2+1);
	}
	else
	{
		return "";
	}
}

function keyProcessNumber()
{
    if ((window.event.keyCode==46)) //|| (window.event.keyCode==48))
    {
		var src     = window.event.srcElement;
		var strTemp = src.value;
		if (strTemp.length==0) return false;
		for(var intLoop=0;intLoop<strTemp.length;intLoop++)
		     if (strTemp.substr(intLoop,1)==".")  return false;
        return true;
    }
	else
	{
        if ((window.event.keyCode>=48) && (window.event.keyCode<=57))
		{
/*		     if (( (parseFloat(strTemp)<=0) || (strTemp.substr(0,1)=="0") ) && (window.event.keyCode==48) ) return false;    //(strTemp.length==1) && (strTemp.substr(0,1)=="0")
		     else return true;
/*           if ((strTemp.substr(0,1)=="0") && (window.event.keyCode==48))
             {
			       src.value = strTemp.substr(1,strTemp.length);
             }   */
		     return true;
		}
	    else return false;
    }
}

/******************************************************************************************************
Function	: validDate()
Purpose		: Check Date
Parameter	: no
Return		: boolean
First time  : 2001-08-29
Author      : ChenHongRi
******************************************************************************************************/
function validDate()
{
	var src     = window.event.srcElement;
	var strDate = src.value;
	if (strDate.length == 0) return false;
	if ((strDate.length < 10) || (strDate.substr(4,1) != "/") || (strDate.substr(7,1) != "/"))
	{
	     alert(m_commonMsg00000);
		 src.value = "";
		 src.focus();
		 return false;
	}
	return true;
}

/******************************************************************************************************
Function	: intOffsetLeft(intWidth)
Purpose		: get Mouse Offset Left
Parameter	: int intWidth
Return		: Position(Offset Left)
First time  : 2001-09-04
Author      : ChenHongRi
******************************************************************************************************/
function intOffsetLeft(intWidth)
{
    var intX = 0;
	var src = window.event.srcElement;
    if (intWidth==null)
	{
	     intX = window.event.x + src.clientWidth - window.event.offsetX - 5;
    }
    else intX = window.event.x + src.clientWidth - window.event.offsetX - parseInt(intWidth) - 5;
	return intX;
}
/******************************************************************************************************
Function	: intOffsetTop(intHeight)
Purpose		: get Mouse Offset Top
Parameter	: int intHeight
Return		: Position(Offset Top)
First time  : 2001-09-04
Author      : ChenHongRi
******************************************************************************************************/
function intOffsetTop(intHeight)
{
	var intY = 0;
	var src = window.event.srcElement;
	if (intHeight==null)
	{
	     intY = window.event.y + src.clientHeight - window.event.offsetY  - 3;
	}
	else intY = window.event.y + src.clientHeight - window.event.offsetY + parseInt(intHeight) - 3;
	return intY;
}
/******************************************************************************************************
Function    : formatDateTime(dtDate,strTemplate)
Purpose	    : format date
Parameter   : dtDate String of date,strTemplate
Return	    : string of date. format is strTemplate
First time  : 2001-09-12
Author      : ChenHongRi
******************************************************************************************************/
function formatDateTime(dtDate,strTemplate)
{
	try
	{
		var dtDate = new Date(dtDate);
	}
	catch(e)
	{
		return "";
	}
	if( isNaN(dtDate) ) return "";

	var strYear = ""+dtDate.getYear();

	var strMonth= dtDate.getMonth()	+ 1;
	strMonth = s2d(strMonth)

	var strDay  = dtDate.getDate();
	strDay = s2d(strDay)

	var strHour = dtDate.getHours()
	strHour = s2d(strHour)

	var strMin = dtDate.getMinutes()
	strMin = s2d(strMin)
	var strSec = dtDate.getSeconds()
	strSec = s2d(strSec)
	var strDate

	strDate = strTemplate.replace(/yyyy/ig,strYear)
	strDate = strDate.replace(/MM/g,strMonth)
	strDate = strDate.replace(/dd/ig,strDay)
	strDate = strDate.replace(/hh/ig,strHour)
	strDate = strDate.replace(/mm/g,strMin)
	strDate = strDate.replace(/ss/ig,strSec)

	return strDate
}

function s2d(strSrc)
{
	var strTemp = parseInt(strSrc)
	if( strTemp < 10 )
		return "0" + strSrc
	else
		return "" + strSrc
}
/************************************
Method:		CheckNumber(bBackSpace)
purpose:		校验文本输入框中键入的是否为数字字符
parameters :
return value :	如果输入为数字返回true; 否则返回false.
************************************/
function CheckNumber(bBackSpace)//OK.
{
	var objSrc = event.srcElement;
	if (objSrc.tagName == "INPUT")
	{
	   if ((bBackSpace!=null) && (bBackSpace==true))
	   {
			if ((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode = 8))
				 return true;
			else return false;
	   }
	   else
	   {
			if (event.keyCode >= 48 && event.keyCode <= 57) return true;
			else return false;
	   }
	}
	return false;
}

/************************************
Method:		keyProcessFun()
purpose:		校验文本输入框中键入的是否为数字或小数点
parameters :
return value :	如果整数返回true
			如果非整数返回false
************************************/
function keyProcessFun() //OK.
{
	if (event.srcElement.tagName == "INPUT")
	{
		if (event.srcElement.value.indexOf(".") == -1)
		{
			if ((event.keyCode >= 48 && event.keyCode <= 57) || event.keyCode == 46)
				return true;
			else
       			{
            			return false;
       			}
    		}
    		else
		{
			if(event.keyCode >=48 && event.keyCode <=57)
				return true;
    			else
    			{
       				return false;
    			}
		}
  	}
}

/************************************
Method:		enableTxtInput(objTxt)
purpose:		改变文本框的状态为:允许输入(类似于Disable = false)
parameters :	objTxt: 输入框对象
return value :
************************************/
function enableTxtInput(objTxt)//OK.
{
	//objTxt.className = "TxtInput";
	objTxt.readOnly = false;
	objTxt.disabled = false;
	//objTxt.tabIndex = objTxt.tmpIndex;
	return true;
}

/************************************
function:		disableTxtInput(objTxt)
purpose:		改变文本框的状态为:禁止输入(类似于Disable = true)
parameters :	objTxt: 输入框对象
return value :
************************************/
function  disableTxtInput(objTxt)//OK.
{
	//objTxt.className = "disabledInput"
	objTxt.readOnly = true;
	objTxt.disabled = false;
	//objTxt.tmpIndex = objTxt.tabIndex;
	//objTxt.tabIndex = -1;
	return true;
}

/************************************
function:		disableSelect(objSel)
purpose:		改变下拉框的状态为:禁止选择
parameters :	objSel: 下拉框对象
return value :
************************************/
function  disableSelect(objSel)//OK.
{
//	objSel.style.className = "disabledSel";
//	objSel.style.background = "#dedede";
//	objSel.style.fontColor  = "black";
//	objSel.style.fontSize   = "9pt";
//	objSel.style.fontFamily = "Tahoma,宋体";
//	objSel.style.height = 21;
	objSel.disabled = true;

	return true;
}

/************************************
function:		enableSelect(objSel)
purpose:		改变下拉框的状态为:允许选择
parameters :	objSel: 下拉框对象
return value :
************************************/
function  enableSelect(objSel)//OK.
{
	//objSel.style.background = "white"
	objSel.disabled = false;
	//objSel.style.className = "selList"
	return true;
}
/******************************************
function	: 	convertToHtmlTag (strProcess)
purpose	: 	转换字符串中特殊符号，以防止被作为HTML语言解释。如：< >
parameter :	strProcess   字符串
return	: 	转换后字符串
******************************************/
function  convertToHtmlTag(strProcess)//OK.
{
	var strResult="";
	for (var iFlag=0;iFlag<=strProcess.length;iFlag++)
	{
		switch (strProcess.charAt(iFlag))
		{
			case "<":
				strResult=strResult+"&lt;";
      	    			break;
			case ">":
	     			strResult=strResult+"&gt;";
	        		break;
	        	case " ":
	        		strResult=strResult+"&nbsp;";
	        		break;
	        	case "&":
	        		strResult=strResult+"&amp;";
	        		break;
	     		default:
	    		        strResult=strResult + strProcess.charAt(iFlag);
	        		break;
		}
   	}
   	return strResult;
}
/****************************************************
function	:	now(strD)
purpose	:	Get current date
parameter	:	strD        String of date
return	:	string of date. format: "mm/dd/yy"
****************************************************/
function  now(strD)//OK.
{
 	var dateObj
 	if (strD == "")
 		return "";
 	if(strD == null)
 		dateObj = new Date();
 	else
 	{
 		dateObj = new Date(strD);
 		if (isNaN(dateObj))
 			return "";
 	}
	var strDate = "";
	strDate += (dateObj.getMonth()>8)? dateObj.getMonth()+1: "0"+(dateObj.getMonth()+1);
	strDate += "/";
	strDate += (dateObj.getDate()>9)? dateObj.getDate(): "0" +dateObj.getDate();
	strDate += "/";
	strDate += dateObj.getFullYear();
	return strDate;
}
/****************************************************
function	:	time(strD)
purpose		:	Get current time
parameter	:	strD        String of time
return		:	string of date. format: "hh:mm:ss"
****************************************************/
function  time(strD)//OK.
{
 	var dateObj
 	if (strD == "")
 		return "";
 	if(strD == null)
 		dateObj = new Date();
 	else
 	{
 		dateObj = new Date(strD);
 		if (isNaN(dateObj))
 			return "";
 	}
	var strDate = "";
	strDate += (dateObj.getHours()>9)? dateObj.getHours(): "0" + (dateObj.getHours());
	strDate += ":";
	strDate += (dateObj.getMinutes()>9)? dateObj.getMinutes() : "0" + dateObj.getMinutes();
	strDate += ":";
	strDate += (dateObj.getSeconds()>9)? dateObj.getSeconds() : "0" + dateObj.getSeconds();
	return strDate;
}
/************************************
function:		objectStartX(obj)
purpose:		返回元素对像在窗口中 Left 坐标值
parameters :	obj: 对象
return value :	对象左上脚的X 坐标
************************************/
function  objectStartX(obj)//OK.
{
	var objTmpParent=obj.offsetParent;
	var iX = obj.offsetLeft;
	while(objTmpParent.tagName!="BODY")
	{
		iX = iX + objTmpParent.offsetLeft;
		objTmpParent = objTmpParent.offsetParent;
	}
	return iX;
}

/************************************
function:		objectStartY(obj)
purpose:		返回元素对像在窗口中 Top 坐标值
parameters :	obj: 对象
return value :	对象左上脚的Y 坐标
************************************/
function  objectStartY(obj)//OK.
{
	var objTmpParent=obj.offsetParent;
	var iY = obj.offsetTop;
	while(objTmpParent.tagName!="BODY")
  	{
    		iY = iY + objTmpParent.offsetTop;
    		objTmpParent = objTmpParent.offsetParent;
  	}
  	return iY;
}

/************************************
function:		objectEndX(obj)
purpose:		返回元素对像在窗口中 Right 坐标值
parameters :	obj: 对象
return value :	对象右下脚的X 坐标
************************************/
function  objectEndX(obj)//OK.
{
	var iX =  objectStartX(obj);
  	iX = iX + obj.clientWidth + 2;
  	return iX;
}

/************************************
function:		objectStartY(obj)
purpose:		返回元素对像在窗口中 Bottom 坐标值
parameters :	obj: 对象
return value :	对象右下脚的Y 坐标
************************************/
function  objectEndY(obj)//OK.
{
  	var iY =  objectStartY(obj);
  	iY= iY + obj.clientHeight + 2;
  	return iY;
}

/************************************
function:		disableButton(objBtn)
purpose:		Disabled button
parameters :	Object of button
return value :
First time  : 2002-07-05
Author      : ChenHongRi
************************************/
function  disableButton(objBtn)
{
	objBtn.className = "headbtn"
	objBtn.disabled = true;
	//objBtn.tmpIndex = objBtn.tabIndex;
	//objBtn.tabIndex = -1;
	return true;
}
/****************************************************
function:		enableButton(objBtn)
purpose:		Enabled button
parameters :	Object of button
return value :
First time  : 2002-07-05
Author      : ChenHongRi
****************************************************/
function enableButton(objBtn)
{
	objBtn.className = "headbtn";
	objBtn.disabled = false;
	//objBtn.tabIndex = objBtn.tmpIndex;
	return true;
}
/****************************************************
function:		inactiveSearchBtn(btnObj)
purpose:		Inactive search button
parameters :	Object of button
return value :
****************************************************/
function inactiveSearchBtn(btnObj)
{
	btnObj.children(0).className = "Inactive";
	btnObj.disabled = true;
	return false;
}

/****************************************************
function:		activeSearchBtn(btnObj)
purpose:		Active search button
parameters :	Object of button
return value :
****************************************************/
function activeSearchBtn(btnObj)
{
	btnObj.children(0).className = "";
	btnObj.disabled = false;
	return false;
}
/****************************************************
function:		disableMenu()
purpose:		Disabled menu
parameters :
return value :
****************************************************/
function disableMenu()
{
	if (typeof  top.menu == "undefined" && typeof  top.menu.divContain == "undefined")
		return -1;
	top.menu.divContain.style.visibility = "visible";
	return 0;
}

/****************************************************
function:		enableMenu()
purpose:		Enabled menu
parameters :
return value :
****************************************************/
function enableMenu()
{
	if (typeof top.menu == "undefined" || typeof top.menu.divContain == "undefined")
		return -1;
	top.menu.divContain.style.visibility = "hidden";
	return 0;
}
/****************************************************
function:		setTitleImage(imgName)
purpose:		Set image of title.
parameters :	Image's name.
return value :	true;
****************************************************/
function setTitleImage(imgName)
{
	top.menu.imgMenuTitle.src = imgName;
	return true;
}
/****************************************************
function:		returnHistory()
purpose:		return Interface of History
parameters :
return value :
****************************************************/
function returnHistory(strUrl)
{
	if (strUrl==null) top.main.history.back();
	else top.main.location=strUrl;
}
/****************************************************
function:		exit()
purpose:		Quit system.
parameters :
return value :
****************************************************/
function exit(strPrefix,strModule)
{
    if (strPrefix==null) strPrefix="../";
	returnMoudle(strModule,strPrefix);
}
/****************************************************
function:		quit()
purpose:		Quit system.
parameters :
return value :
****************************************************/
function quit(strModule)
{
	returnMoudle(strModule,"");
}

/****************************************************
function:		checkPhone()
purpose:		only input "(" , ")" , "-" , "/" , "0~9"
parameters :
return value :
****************************************************/
function checkPhone(bBackSpace)
{
	if (event.srcElement.tagName == "INPUT")
	{
		var keyCode = event.keyCode;
  	    if ((bBackSpace!=null) && (bBackSpace==true))
	    {
			if ((keyCode >= 48 && keyCode <= 57) || keyCode == 47 || keyCode == 45 || keyCode == 40 || keyCode ==41 || keyCode ==8)
				return true;
			else return false;
        }
		else
		{
			if ((keyCode >= 48 && keyCode <= 57) || keyCode == 47 || keyCode == 45 || keyCode == 40 || keyCode ==41)
				return true;
			else return false;
		}
  	}
}
/****************************************************
function:		right(strString,iLength)
purpose:		取字符串的右边几位
parameters :	strString,iLength
return value :	字符串
First time  :   2002-06-24
Author      :   ChenHongRi
****************************************************/
function right(strString,iLength)
{
	if(typeof strString!= "string") return "";
	if(typeof strString == "string" && strString.toUpperCase() == "NULL")
	{
		return "";
	}
	if((strString == null)||(strString == "<NULL>"))	return "";
	strString=trim(strString);
	return strString.substring(strString.length-iLength,strString.length);
}

/****************************************************
function:		left(strString,iLength)
purpose:		取字符串的左边几位
parameters :	strString,iLength
return value :	字符串
First time  :   2002-06-24
Author      :   ChenHongRi
****************************************************/
function left(strString,iLength)
{
	if(typeof strString!= "string") return "";
	if(typeof strString == "string" && strString.toUpperCase() == "NULL")
	{
		return "";
	}
	if((strString == null)||(strString == "<NULL>"))	return "";
	strString=trim(strString);
	return strString.substring(0,iLength);
}
/************************************************
function:		isDate(strD)
purpose:
parameters :	strD:	string of date
return value :	TRUE / FALSE
*************************************************/
function isDate(strD)
{
	if (strD == "" || strD == null)
		return false;
	var strDate = now(strD)
	if (strDate == "" || strDate != strD)
		return false;
	else
		return true;
}

/************************************************
function:		isNumber(objName)
purpose:		is number
parameters :	objName:object ID
return value :	TRUE---number
			FALSE---no number
*************************************************/
function  isNumber(objName)
{
	var strS;
	var iLoop;
	var iBool=1;
	var strTmp;
	var iD=0;
	var iK=0;

	strS=objName.value;
        if(strS=="")return false;
	for (iLoop=0;iLoop<strS.length;iLoop++)
	{
		strTmp=strS.charAt(iLoop);
		if (!((strTmp=="-")||(strTmp==".")||((strTmp>="0")&&(strTmp<="9"))))
		{
			iBool=0;
			break;
		}
		else
		{
			if(strTmp=="-")
			{
				iK++;
				if (iK>1)
				{
					iBool=0;
					break;
				}
				if(iLoop!=0)
				{
					iBool=0;
					break;
				}
			}
			if(strTmp==".")
			{
				iD++;
				if(iD>1)
				{
					iBool=0;
					break;
				}
				/*if((iLoop==(strS.length -1))||(iLoop=="0"))
				{
					iBool=0;
					break;
				}*/
			}

		}
	}
	if (iBool==0)
		return false;
	else
		return true;
}
/***************************************
return value :	TRUE---number
			FALSE---no number
*************************************************/
function  isNumber2(objName)
{
	var strS;
	var iLoop;
	var iBool=1;
	var strTmp;

	strS=objName.value;
        if(strS==""){return false;}
	for (iLoop=0;iLoop<strS.length;iLoop++)
	{
		strTmp=strS.charAt(iLoop);
		if (!((strTmp>="0")&&(strTmp<="9")))
		{
			iBool=0;
			break;
		}

	}
	if (iBool==0)
		return false;
	else
		return true;
}
/****************************************************
function	:	memoCheck(txtObj,iLength)
purpose		:	custom  length for txtObj
parameter	:	txtObj    Object; iLength: string length
return		:	false or true
****************************************************/
function memoCheck(txtObj,iLength)
{
    var strTxt,intLength;
    strTxt=txtObj.value;

    if (iLength==null) intLength=255;
    else intLength=iLength;

    if (strTxt.length>intLength)
     {
         txtObj.value=strTxt.substr(0,intLength-1);
         return false;
     }
    else return true;
}
/****************************************************
function:		saveFilterUrltoMenu(strUrl,strMode)
purpose:		Save Filter url to menu
parameters :	strUrl
return value :	no
Author      :   ChenHongRi
First time  :   2002-08-02
****************************************************/
function saveFilterUrltoMenu(strUrl,strMode,strAction)
{
	try
	{
		if (strUrl!=null)
		{
			top.menu.frmWriteXls.hidFilterUrl.value = strUrl;
		}
		else top.menu.frmWriteXls.hidFilterUrl.value = top.main.location;

		if (strMode!=null) // 2003-10-20 Chr
		{
			top.menu.frmWriteXls.hidMode.value = strMode;
		}
		else top.menu.frmWriteXls.hidMode.value = "";

		if (strAction!=null) // 2003-10-20 Chr
		{
			top.menu.frmWriteXls.hidAction.value = strAction;
		}
		else top.menu.frmWriteXls.hidAction.value = "";
	}
	catch(e)
	{
	}
}
function isID(str)	//检查身份证 added by zy
{
	var regexp = /^([0-9]{0}|[0-9]{15}|[0-9]{18})$/;
	return regexp.test(str);
}
//判断是否存在fa,gl,wa模块
function HaveGL(iFlag)
{
//	return (top.hide.g_iModule & iFlag);
	return (top.hide.g_iModule | iFlag);    // 2002-6-26 Chr
}
/******************************************************************************************************
Function	: function getSubstr(strSource,intStartPosition,intOverPosition,cPosition)
Purpose		: get sub string in string
Parameter	: strSource,intStartPosition,intOverPosition,cPosition
Return		: String
First time  : 2002-10-30
Author      : ChenHongRi
******************************************************************************************************/
function getSubstr(strSource,intStartPosition,intOverPosition,cPosition)
{
	var strTarget = "";
	var cTemp     = "";
	var intLoop   = 0;
	var intCount  = 0;
	try
	{
	    strSource = trim(strSource);
		intCount  = strSource.length;
		if (intStartPosition>intCount) intStartPosition = intCount;
		if (intOverPosition>intCount)  intOverPosition  = intCount;
		if (cPosition==null)
		{
			strTarget = strSource.substring(intStartPosition,intOverPosition);
		}
		else
		{
		    if (intStartPosition<=intOverPosition)
			{
				for (intLoop=intStartPosition;intLoop<intOverPosition;intLoop++)
				{
					cTemp = strSource.substr(intLoop,1);
					if (cTemp==cPosition) break;
					else strTarget = strTarget + cTemp;
				}
		    }
			else
			{
				for (intLoop=intStartPosition-1;intLoop>intOverPosition;intLoop--)
				{
					cTemp = strSource.substr(intLoop,1);
					if (cTemp==cPosition) break;
					else strTarget = cTemp+strTarget;
				}
			}
		}
	}
	catch(e)
	{
	}
	return strTarget;
}


/************************************
Method:		keyProcessTimeFun()
purpose:		校验文本输入框中键入的是否为数字或":"   这样是时间的格式  19:20:30
parameters :	   add by sammchen
return value :	如果整数返回true
			如果非整数返回false
************************************/
function keyProcessTimeFun(bBackSpace) //OK.
{
	var objSrc = event.srcElement;
	var strTemp = objSrc.value;
	if (objSrc.tagName == "INPUT")
	{
		if ((bBackSpace!=null) && (bBackSpace==true))  // ":" asII 码 58
		{
			if ((event.keyCode >= 48 && event.keyCode <= 58) || event.keyCode == 8)
			{
				//alert(strTemp);
				return true;
			}
				//return true;
			else
       			{
            			return false;
       			}
    		}
    		else
		{
			if(event.keyCode >=48 && event.keyCode <=58)
			{
				//alert(strTemp);
				return true;
				}
    			else
    			{
       				return false;
    			}
		}
  	}
	return false;
}

/************************************
Method:		FormatTimeFun()
purpose:		格式化这样是时间的格式  19:20:30
parameters :	   add by sammchen
return value :	返回string

************************************/
function FormatTimeFun(strTime) //OK.
{

	var strTemp = strTime;
	if(strTemp==null)strTemp="";
	var strValue = "10:00:00";
	var intLeng = strTemp.length;
	var strFirst = "";
	var strMid = "";
	var strEnd = "";

	if (strTemp == "")
	{
		return false;
	}
	else
	{
		if (intLeng < 8)
		{
			return false;
		}
		else
		{
			if (strTemp.substring(2,3) != ":" || strTemp.substring(5,6) != ":")
			{
				return false;
			}
			else
			{
				if ( (strTemp.substring(0,2) >= 0 && strTemp.substring(0,2) <= 23 ) || (strTemp.substring(3,5) >= 0 && strTemp.substring(3,5) <= 23 ) || (strTemp.substring(6,8) >= 0 && strTemp.substring(6,8) <= 23 ) )
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
	}

}

/************************************
Method:		FormatTimeFun()
purpose:		格式化这样是时间的格式  19:20:30
parameters :	   add by gaoxiaohong
return value :	返回string

************************************/
function FormatTimeObj(obj) //OK.
{

	var strTemp = obj.value;
	var isRight = true;
	var strValue = "10:00:00";
	var intLeng = strTemp.length;
	var strFirst = "";
	var strMid = "";
	var strEnd = "";
	if (strTemp == "")
	{
		obj.value="00:00:00";
	}


	if (intLeng !=  8 )
	{
		isRight = false;
	}

	if (strTemp.substring(2,3) != ":" || strTemp.substring(5,6) != ":")
	{
		isRight = false;
	}

	var hour = getValidStr(strTemp.substring(0,2));
	var minute = getValidStr(strTemp.substring(3,5));
	var second = getValidStr(strTemp.substring(6,8));

   if(isNaN(hour) || isNaN(minute) || isNaN(second))
   {
	  isRight = false;
   }
	if ( (new Number(hour) < 0 || new Number(hour) > 23 ) || (new Number(minute) < 0 && new Number(minute) > 23 ) || (new Number(second) <0 && new Number(second) > 23 ) )
	{

		isRight = false;
	}

	if(isRight == false)
	{
		obj.value = "00:00:00";
		obj.select();
		alert("时间格式不对，请输入正确的时间形式如：12:05:09");
	}

}

/******************************************************************************************************
Function	: function  getValidStr(str) 为 function FormatTimeObj(obj) 提供 将07转换为7
Purpose		: get sub string in string
Parameter	: strSource,intStartPosition,intOverPosition,cPosition
Return		: String
First time  :  2004-04-22
Author      : GaoXiaoHong
******************************************************************************************************/
function getValidStr(str)
{
	if(str.length == 2)
	{
	   if(str.substring(0,1) == "0")
		{
		   str = str.substring(1,2);
		}
	}
	return str;

}
/******************************************************************************************************
Function	: function getAdjunctName(String)  获得附件的名字和上传的文件的名字
Purpose		: get sub string in string    存放的名字为 （用户输入的文件名+'$'+用户号+部门、科室号+日期(精确到分和秒)+序号+扩展名）
Parameter	: strSource,intStartPosition,intOverPosition,cPosition
Return		: String
First time  : add by 2003-6-24
Author      : ChenJunSong
******************************************************************************************************/
function getAdjunctName(StrAdjunctName)
{
	//var tt = 'java中文$dept20034323434343.txt';
	var t_name = "";
	if (StrAdjunctName == '')	return "";

	var t_leng = StrAdjunctName.length;
	var t_start = StrAdjunctName.indexOf('$');
	var t_end = StrAdjunctName.indexOf('.');
	if (t_start == -1)	return "";
	if (t_end == -1)	return "";

	t_name = StrAdjunctName.substring(0,t_start) + StrAdjunctName.substring(t_end,t_leng);
	return t_name;

}


/******************************************************************************************************
Function	: function getFileName(String)  获得附件的名字和上传的文件的名字
Purpose		: get sub string in string    存放的名字为 （d:\java\app\tt.txt）
Parameter	: strSource,intStartPosition,intOverPosition,cPosition
Return		: String
First time  : add by 2003-6-24
Author      : ChenJunSong
******************************************************************************************************/
function getFileName(StrAdjunctName)
{
	//var tt = 'java中文$dept20034323434343.txt';
	var t_name = "";
	if (StrAdjunctName == '')	return "";

	var t_leng = StrAdjunctName.length;
	var t_start = StrAdjunctName.lastIndexOf('\\')+1;
	var t_end = StrAdjunctName.indexOf('.');
	if (t_start == -1)	return "";
	if (t_end == -1)	return "";

	t_name = StrAdjunctName.substring(t_start,t_leng);
	return t_name;

}
/************************************
Function     : changeRowColor(objName)
purpose      : 使TABLE 中当行的高亮显示，使原高亮显示的行复原
parameters   : objName: TABLE ID 号 bMove=true 设置行的color,false 取消行的color
return value :
First time   : 2003-8-31 22:16
Author       : ChenHongRi
************************************/
function changeRowColor(objName,bMove,oldclass)
{

	try
	{

  		if (bMove)
		{
			objName.className = "list_table";
			/*
			if(objName.children.length>0)
			   {
                   for(var i=0;i<objName.children.length;i++)
			        {
					     var objTD = objName.children[i];
						 alert(objTD.tagName);
						 if(objTD.children.length>0)
						    {

						    }
				    }
			   }
			   */

		}
		else
		{
			objName.className =oldclass ;
		}
		if(!bMove)
		    OldObj.className = oldclass;
	}
	catch(e)
	{
	}
}
/************************************
Function     : SelectRow(objName,keyNo)
purpose      : 选定一行，使行的颜色变化，并将原来的清除，保留住选定的keyNo
@param       : objName: 行对象ID 号
@param       : KeyNo:对应的行记录的主键值
return value :	无
First time   : 2004-4-1
Author       : Milkli
************************************/
var OldObj = null;
function SelectRow(objName,keyNo,oldclass)
{
	if(objName!=null) objName.className = "list_table";
	OldObj = objName;
	OldObj.className = oldclass;
	//alert(document.all("topicNo"));
	if(document.all("topicNo")!=null)
	  document.all("topicNo").value = keyNo;
}
//var OldObj = null;
function SelectRow_1(objName,clsname,oldclsname,oldclass)
{
	try
	{
		if(oldclsname==null)oldclsname = "areaTD";
		//改变失焦后的行的颜色
		if(OldObj!=null)
		  {
			    OldObj.className="\'"+oldclsname+"\'";
				if(OldObj.cells.length>0)
				{
					 for(var i=0;i<OldObj.cells.length;i++)
					   {

                           OldObj.cells[i].className=oldclsname;
						   if(OldObj.cells[i].children.length>0)
						     {
							     for(var j=0;j<OldObj.cells[i].children.length;j++)
								   {
									   OldObj.cells[i].children(j).className=oldclsname;
								   }
						     }
					   }
				}

		  }
		while(objName.tagName!='TR') objName = objName.parentElement;
		if(clsname==null) clsname=oldclass;
		objName.className="\'"+clsname+"\'";
		if(objName.cells.length>0)
		{
			 for(var i=0;i<objName.cells.length;i++)
			   {

                   objName.cells[i].className=clsname;
				   if(objName.cells[i].children.length>0)
				     {
					     for(var j=0;j<objName.cells[i].children.length;j++)
						   {
							   objName.cells[i].children(j).className=clsname;
						   }
				     }
			   }
		}
		OldObj = objName;
		/*
		if(OldObj!=null) OldObj.className = "listbody";
		OldObj = objName;

		alert("classname="+clsname);
		OldObj.className=clsname;
		*/
	}
	catch(e)
	{}


}
/****************************************************
Function     : FrameSelectRow(objName,keyNo)
purpose      : 在IFrame的情况下选定一行，使行的颜色变化，并将原来的清除，保留住选定的keyNo
@param       : objName: 行对象ID 号
@param       : frameObj: 包含Frame的上级窗口，调用时一般用parent即可
@param       : KeyNo:对应的行记录的主键值
return value :	无
First time   : 2004-4-2
Author       : Milkli
*****************************************************/
function FrameSelectRow(objName,frameObj,keyNo){
	if(OldObj!=null) OldObj.className = "listbody";
	OldObj = objName;
	OldObj.className = "listbody3";
	frameObj.document.all("topicNo").value = keyNo;

}

//Milkli add 2004-3-20
function NavigatePage(Url){
	//  alert(Url);
 	if(Url.length>0){
  	    window.location = Url;
  	}
  	else
  	    return;
}
/******************************************************************************************************
Function     : getSelectChkBox(obj,strChkName)
purpose      : 检测在查询页面中是否有选中的Checkbox
parameters   : obj:页面中的checkbox对象
return value : 0:未选中；1:有选中
First time   : 2003-11-15 17:35
Author       : ChenHongRi
******************************************************************************************************/
function getSelectChkBox(obj,strChkName)
{
	var iFlag = 0;
	try
	{
	    if (obj==null) obj = document;
		if (strChkName==null) strChkName = "chkbox";

	    var objChk;
	    var aObjInput = obj.getElementsByTagName("INPUT");

		for (var iLoop=0;iLoop<aObjInput.length; iLoop++)
		{
		    var objChk = aObjInput[iLoop];
		    if (objChk.type.toUpperCase( )!="CHECKBOX") continue;
			else
			{
		        if((objChk.name==strChkName) && (objChk.checked))
				{
			       iFlag=1;
				   break;
				}
			}
		}
	}
	catch(e)
	{
		alert(e.description);
	}
    return iFlag;
}
/******************************************************************************************************
Function     : getSelectChkBox(obj,strChkName)
purpose      : 根据查询页面中指定的Checkbox选中页面内其它Checkbox
parameters   : objSelf:指定的Checkbox对象，obj:页面中的对象(如form),strChkName:checkbox name
return value : no
First time   : 2003-11-15 23:53
Author       : ChenHongRi
*******************************************************************************************************/
function selectAllChkBox(objSelf,obj,strChkName)
{
	try
	{
	    if (obj==null) obj = document;
		if (strChkName==null) strChkName = "chkbox";

	    var objChk;
	    var aObjInput = obj.getElementsByTagName("INPUT");

		for (var iLoop=0;iLoop<aObjInput.length; iLoop++)
		{
		    var objChk = aObjInput[iLoop];
		    if (objChk.type.toUpperCase( )!="CHECKBOX") continue;
			else
			{
		        if(objChk.name==strChkName)
				objChk.checked=objSelf.checked;
			}
		}
	}
	catch(e)
	{
		alert(e.description);
	}
}

function selectChkBox(objSelf,obj)
{
	try
	{
	    if (obj==null) obj = document.frmAction.chkbox;
        var iLength = parseInt(obj.length);
        if(isNaN(iLength)) iLength = 1;
		if(iLength ==1)
		{
			obj.checked = objSelf.checked;
			return;
		}
		for (var iLoop=0;iLoop<iLength; iLoop++)
		{
		    var objChk = obj[iLoop];
			objChk.checked=objSelf.checked;
		}
	}
	catch(e)
	{
		alert(e.description);
	}
}
/******************************************************************************************************
Function     : StrLength(Str)
purpose      : 取得指定数据串的长度，一个中文按两个字符计算
parameters   : Str:欲计算长度的字符串
return value : 字符串的长度
First time   : 2004-3-31
Author       : Milkli
*******************************************************************************************************/
function StrLength(str){
	var Str = new String(str);
	var slen= Str.length;
	var ireturnlen = 0;
	var itest = "";
	for(var i=0;i<slen;i++){
		itest = Str.charAt(i);
		if(itest>"~") {ireturnlen +=2;}
		else          ireturnlen++;
	}
	return ireturnlen;
}

/*******************************************
 * 在屏幕正中打开一个新的窗口
 * @param psUrl 欲打开文件的Url
 * @param psFrame 打开的新窗口的名称
 * @param piWidth 打开窗口的宽度
 * @param piHeight 打开窗口的高度
 * @author:Milkli
 * Time:2004-02-09
 **********************************************/
function openWindow(psUrl,psFrame,piWidth,piHeight){
    var iScreenWidth=screen.availWidth;
    var iScreenHeight=screen.availHeight;
    var iLeft=0;
    var iTop=0;
    iLeft=(iScreenWidth-piWidth)/2;
    iTop=(iScreenHeight-piHeight)/2;
    var sFeatures="resizable=1,toolbar=0,location=0,status=0,menubar=0,scrollbars=1,width="+piWidth;
    sFeatures+=",height="+piHeight+",left="+iLeft+",top="+iTop;
    window.open(psUrl,'',sFeatures);
	return true;
}

/****************************************************
Function     : JumpTopage(page)
purpose      : 翻页程序中跳转至指定的页
@parame page : 目标页码
return value : 元
First time   : 2004-4-1
Author       : Milkli
Description  :此函数结合TuranPage.class中的getHeadString()函数使用
*****************************************************/
function JumpTopage(page)
{
	document.frmAction.currentpage.value = page;
	document.frmAction.acton = "";
	document.frmAction.submit();
}

/******************************************
 *菜单页面显示子菜单时鼠标移上时的事件
 *Milkli 2004-4-22
 *******************************************/
function rows_onmouseover()
{
	var src = event.srcElement;
	while (src.tagName != "TR")src = src.parentElement;
	if(src.tagName == "TR") src.className = "Inactive";
	return true;
}
/***********************************************
 *菜单页面显示子菜单时鼠标移开时的事件
 *Milkli 2004-4-22
 ***********************************************/
function rows_onmouseout()
{
	var src = event.srcElement;
	while (src.tagName != "TR")src = src.parentElement;
	if(src.tagName == "TR") src.className = "Active";
	return true;
}

/****************************************************
Function     : checkCount(str)
purpose      :  校验数字
@parame page :
return value : 目标字符串
First time   : 2004-4-21
Author       : GAOXIAOHONG
Description  :此函数结合日历函数使用
*****************************************************/
function checkCount(obj)
{
	var temp = obj.value;
	if(isNaN(temp))
	{
		obj.select();
		alert("请输入数字！");
	}

}
 function checkCount2(obj)
{
	var temp = obj.value;
	if(isNaN(temp))
	{
		obj.select();
                return false;
	}
    return true;
}

/*******************************************************
 *   字符替换函数
 * 将source中的字符oldchar换为newchar
 * 注意：可以将字符替换为字符串，不能对字符串进行替换
 *******************************************************/
function Replace(source,oldchar,newchar){
	var str ="";
	for(var i=0;i<source.length;i++){
		if(source.charAt(i) == oldchar) str += newchar;
		else                            str += source.charAt(i);
	}
	return str;
}



function deleteitem(obj)
{
   try
   {
     var src = obj.parentElement;
     while(src.tagName!="TR")
       {
          src = src.parentElement;
        }
     var intRow = src.rowIndex;
     personlist.deleteRow(intRow);
     document.all.countindex.value =eval(document.all.countindex.value) - 1;

     var vatablePersoninfo=document.all.personlist;
     var tablelength=vatablePersoninfo.rows.length;
     for (i=intRow;i<tablelength;i++)
      {
      	 vatablePersoninfo.rows[i].cells[0].innerHTML="<div align='center'><label class='FieldLabel'>"+i+"<label></div>";
       }

     var optionlength = document.all.appendInfoItem.options.length;
     var indexcount = optionlength/eval(document.all.countindex.value);

     for (i=optionlength-1;i>=(intRow-1)*indexcount;i--)
     {
     	if (document.all.appendInfoItem.options[i].text != intRow)
      	  continue;
        document.all.appendInfoItem.options.remove(i);
      }

    for (i=optionlength-indexcount-1;i>=(intRow-1)*indexcount;i--)
      {
      	document.all.appendInfoItem.options[i].text = eval(document.all.appendInfoItem.options[i].text) - 1;
       }
   }
   catch(e)
   {
     promptInfo(100);
   }
}

function displayPerson()
{
  if (document.all.appendInfoItem.options.length>0)
   {
      var isModify = document.all.isVieworModify.value;
      var vatablePersoninfo=document.all.personlist;
      var tablelength=vatablePersoninfo.rows.length;
      var r = vatablePersoninfo.insertRow(tablelength);
      r.ClassName="uiDetailTable";
      var j = 0;
      var cellnum = 0;
      for (i=0;i<document.all.appendInfoItem.options.length;i++)
      {
        preValue = document.all.appendInfoItem.options[i].text;
        if (cellnum == 0)
         {
          insertinfo=r.insertCell(cellnum);
          insertinfo.innerHTML="<div align='center'><label class='FieldLabel'>"+(j+1)+"<label></div>";
          }
        cellnum += 1;
        insertinfo=r.insertCell(cellnum);
        insertinfo.innerHTML="<div align='center'><label class='FieldLabel'>"+document.all.appendInfoItem.options[i].value+"<label></div>";
        if (i != (document.all.appendInfoItem.options.length - 1))
         {
            nextValue = document.all.appendInfoItem.options[i+1].text;
            if (nextValue != preValue)
             {
              cellnum += 1;
              insertinfo=r.insertCell(cellnum);
              if (isModify == "1")
                insertinfo.innerHTML="<div align='center' style='cursor:hand'><img src='../../image/uidelete.gif' width='16' height='12' border=0 onclick='deleteitem(this)'></div>";
              j += 1;
              var vatablePersoninfo=document.all.personlist;
              var tablelength=vatablePersoninfo.rows.length;
              var r = vatablePersoninfo.insertRow(tablelength);
              r.ClassName="uiDetailTable";
              cellnum = 0;
             }
          }
        else
          {
              cellnum += 1;
              insertinfo=r.insertCell(cellnum);
              if (isModify == "1")
              insertinfo.innerHTML="<div align='center' style='cursor:hand'><img src='../../image/uidelete.gif' width='16' height='12' border=0 onclick='deleteitem(this)'></div>";
           }
       }
    document.all.countindex.value = j + 2;
    }
 }
/****************************************************
function	:	subStr(str)
author      :   黄欧
purpose		:	取特定长度的字串
parameter	:	String s
return		:	int
desc        :
****************************************************/
function subStr(s,begin,end)
{
	var len=StrLength(s);
	var newStr="",curlen=0;
	var unicode=0;
	if(begin>=end) return "";

	if(len<begin || len<end) return "";

	try
	  {
		for(i=0 ;i<s.length;i++)
		  {
			if(curlen>=begin && curlen<end)
			  {
				 if(s.charCodeAt(i)>128)
				   {
					   if((curlen+2)>end)
					     {
						    return newStr;
					     }
				   }
				newStr = newStr+s.charAt(i);
			  }
			unicode=s.charCodeAt(i)
			if (unicode>128)
				curlen=curlen +2   //中文双字节
			else
				curlen=curlen+1

		}
	  }
	catch(Exception)
	  {
	  }


	return newStr
}
	//// this function returns TRUE if a email address is right format
function isEmail(email)
{
 // valid format "a@b.cd"
 invalidChars = " /;,:{}[]|*%$#!()`<>?";
if (email == "")
    {
  return false;
    }
for (i=0; i< invalidChars.length; i++)
    {
  badChar = invalidChars.charAt(i)
  if (email.indexOf(badChar,0) > -1)
         {
   return false;
         }
    }
atPos = email.indexOf("@",1)
 // there must be one "@" symbol
if (atPos == -1)
    {
  return false;
    }
if (email.indexOf("@", atPos+1) != -1)
 {
 // and only one "@" symbol
  return false;
 }
 periodPos = email.indexOf(".",atPos)
if(periodPos == -1)
     {
 // and at least one "." after the "@"
  return false;
     }
if ( atPos +2 > periodPos)
 // and at least one character between "@" and "."
 {
  return false;
 }
 if ( periodPos +3 > email.length)
 {
  return false;
 }
 return true;
}



 function trim(str)
  {
	  str = str.replace(/(^\s*)|(\s*$)/g, "");
	  return str;
  }
  
  function ltrim(str)
  {
	  str = str.replace(/(^\s*)/g, "");
	  return str;
  }
  
  function rtrim(str)
  {
	  str = str.replace(/(\s*$)/g, "");
	  return str;
  }

function showCalendar(sDate){
	var  rValue;
	var  xLeft=event.clientX+window.screenLeft;
	var  yTop=event.clientY+window.screenTop;
	rValue=window.showModalDialog(RootPath+"/js/calendar.jsp",sDate,"dialogHeight: 220px; dialogWidth: 200px; dialogTop: " + yTop + "; dialogLeft: " + xLeft + "; edge: Raised;  help: No; resizable: yes; status: No;scroll:no;");
	if (rValue=="0") {
		return ""
	}
	if (rValue==null ) {

		return sDate
	}
	else{
		
		return rValue
	}
}

function select_date(sDate){

	if (!chkdate(sDate))
		sDate="";
        //var index = window.event.srcElement.sourceIndex -2;
	document.all[window.event.srcElement.sourceIndex -1].value = showCalendar(sDate);

//	return showCalendar(sDate);
}
function chkdate(inputstr){
	yr=inputstr.substring(0,4)
	in1=inputstr.substring(4,5)
	mn=inputstr.substring(5,7)
	in2=inputstr.substring(7,8)
	dt=inputstr.substring(8,10)
	if (in1!="-" || in2!="-"){
		return false
	}

	if (parseFloat(mn)){
		if (mn <1 || mn >12){
			return false;
		}
	}

	if (parseFloat(dt)){
		if (dt<1 || dt>31){
			return false;
		}
	}

	if (parseFloat(yr)){
		if (yr.length < 4 || yr.length > 4){
			return false;
		}
	}

	//fot months with 30 days
	if (mn==4 || mn==6 || mn==9 || mn==11){
		if(dt==31){
			return false;
		}
	}

	//check for leap year

	if (mn==2){
		var leapyr=parseInt(yr/4)
		if (isNaN(leapyr)){
			return false;
		}
		if (dt>29){
			return false;
	}

	if (dt==29 && ((yr/4)!=parseInt(yr/4))){
		return false
	}
	//leap year bracket ends here
	}
	//main brackets end here
	return true
}
function ChkTelCode(num)
{
 for(i_loop=0;i_loop<num.value.length;i_loop++)
  {
    if (!(((num.value.charAt(i_loop)>=0)&&(num.value.charAt(i_loop)<=9))||(num.value.charAt(i_loop)=="(")||(num.value.charAt(i_loop)==")")||(num.value.charAt(i_loop)=="-")||(num.value.charAt(i_loop)==",")))
     {
      alert("电话号码的格式不正确，它只能包括数字、‘，’、‘-’和括号！");
      return false;
     }
    if( ((num.value.charAt(i_loop)==")")&&(num.value.charAt(i_loop+1)!="-" )&&(num.value.charAt(i_loop+1)!=","))||(num.value.charAt(i_loop)=="-")||(num.value.charAt(i_loop)==","))
       {
        if (((num.value.length-i_loop-1)>8)||((num.value.length-i_loop-1)<7))
          {
//           alert("电话号码的长度不正确，在区号之后应该是7位或者8位！");
//           return false;
          }
        }
   }
 if (!((num.value.length>=7)&&(num.value.length<=20)))
    {
      alert("电话号码的长度不正确，它只能大于或等于7位并且小于或等于20位！");
      return false;
     }
     return true;
 }
 function ischinese(s){
  var ret=true;
  for(var i=0;i<s.length;i++)
   ret=ret && (s.charCodeAt(i)>=10000);
   return ret;
}

//返回两个时间的天数:要求比较的时间的格式为：YYYY-MM-DD
function better_time(strDateStart,strDateEnd) {   
    var   strSeparator   =   "-";   //日期分隔符   
    var   strDateArrayStart;   
    var   strDateArrayEnd;   
    var   intDay   
    strDateArrayStart   =   strDateStart.split(strSeparator);   
    strDateArrayEnd   =   strDateEnd.split(strSeparator);   
    var   strDateS   =   new Date(strDateArrayStart[0]   +   "/"   +   strDateArrayStart[1]   +   "/"   +   strDateArrayStart[2]);   
    var   strDateE   =   new Date(strDateArrayEnd[0]   +   "/"   +   strDateArrayEnd[1]   +   "/"   +   strDateArrayEnd[2]);   
    intDay   =   (strDateS-strDateE)/(1000*3600*24)
 
        return  intDay   
}

var oldbdhtml, bdhtml,sprnstr,eprnstr,prnhtml;
   function preview() {
	oldbdhtml=window.document.body.innerHTML;
	var printArea = document.getElementById("printArea");
	if(printArea != undefined){
		printArea.style.width="650";
	}

	bdhtml=window.document.body.innerHTML;
	sprnstr="<!--startprint-->";
	eprnstr="<!--endprint-->";
	prnhtml=bdhtml.substr(bdhtml.indexOf(sprnstr)+17);
	prnhtml=prnhtml.substring(0,prnhtml.indexOf(eprnstr));
	window.document.body.innerHTML=prnhtml;
	window.print();
	window.document.body.innerHTML = oldbdhtml;
}

function closeWindow(){
	try{
		window.opener = null;
		window.open("","_self"); //fix ie7 
	}
	catch(e){
	}
	window.close();
}

function oaErrorTip(){
	//alert("由于新版OA试运行，从OA系统链接到SMS系统进行待办处理时，\r附件的上传下载不能正常使用，请从http://sms.shenzhenair.com登陆！");
}

	// s1: "2007-01-01"
	// s2: sysdate
	// flag: 0 times 1 days 
	function compareDate(s1, flag){	
		  s1 = s1.replace(/-/g, "/");
		  s1 = new Date(s1);
		  var currentDate = new Date();
		  
		  var strYear = currentDate.getYear();
			var strMonth= currentDate.getMonth() + 1;
			var strDay  = currentDate.getDate();
			var strDate = strYear + "/" + strMonth + "/" + strDay;		  
		  s2 = new Date(strDate);
		  
		  var times = s1.getTime() - s2.getTime();
		  if(flag == 0){
		  	return times;
		  }
		  else{
		  	var days = times / (1000 * 60 * 60 * 24);
		  	return days;
		  }
	 } 