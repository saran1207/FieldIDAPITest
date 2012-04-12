<style type='text/css'>
    table.message {
        border-width: 1px ;
        border-spacing: 1px;
        border-style: none;
        border-color: #999999;
        border-collapse: collapse;
        background-color: rgb(255, 255, 240);
        width:900px;
    }
    table.message th {
        border-width: 1px;
        padding: 4px;
        border-style: solid;
        border-color: #999999;
        background-color: #DDDDDD;
        -moz-border-radius: 0px;

    }
    table.message td {
        border-width: 1px;
        padding: 4px;
        border-style: solid;
        border-color: #999999;
        background-color: white;
        -moz-border-radius: 0px;
    }

    tr.overdue td {
        background-color:#FFEBEB;
    }

    table.message th, table.message td {
        font-size: 11px;
        font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
    }

    table.wrapper {
        width:900px;
        background:#FFFFFF;
        border:2px solid #CCCCCC;
    }

    .reportContent {
        padding-bottom: 10px;
        margin-bottom: 10px;
    }

    h1 {
        font-size: 26px;
        font-weight:bold;
        font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
    }

    h3 {
        font-size: 17px;
        font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
        margin-bottom:10px;
    }

    h4 {
        font-size: 12px;
        font-weight:normal;
        padding-top:0px;
        margin-top:0px;
        margin-bottom:10px;
        font-family: "lucida grande", tahoma, verdana, arial, sans-serif;
    }

    body {
        background:#f9f9f9;
    }

</style>

<table width=900 align="center" class="wrapper" >
    <tr><td style="padding: 25px;">

        <div class="reportContent">
            <h1>Search Results</h1>
        </div>

        <#if message?exists>
            <div class="reportContent">
                ${message}
            </div>
        </#if>

        <div class="reportContent">

            <table class="message" cellpadding=2 cellspacing=2 border>
                <tr>
                    <#list columns as column>
                        <th>${column}</th>
                    </#list>
                </tr>

                <#list rows as row>
                    <tr>
                        <#list columns as column>
                            <td>${row.stringValues[column_index]}</td>
                        </#list>
                    </tr>
                </#list>
            </table>

        </div>


</td></tr></table>

