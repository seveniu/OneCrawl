// 定义 tree data 解析
var myjson = webix.DataDriver.myjson = webix.copy(webix.DataDriver.json);
myjson.child = "children";


//定义 datatable 中的 suggest
webix.editors.mySuggest = webix.extend({
    render: function () {
        var node = webix.html.create("div", {
            "class": "webix_dt_editor"
        }, "<input type='text'>");
        var suggestId;
        if (!this.config.collection) {
            webix.message("data not define", "error");
        }
        if (!this.config.suggestTemplate) {
            webix.message("suggestTemplate not define", "error");
        }
        if (this.config.suggestUrl) {
            // 有 url ，每次请求完整数据，并且打开，有collection 后不显示
//            suggestId = webix.ui({view: "suggest", body: {dataFeed: this.config.url,url:this.config.url}})._settings.id;
            suggestId = webix.ui({view: "suggest", body: {template: this.config.suggestTemplate, dataFeed: this.config.suggestUrl, url: this.config.suggestUrl}})._settings.id;
        } else {
            webix.message("url not define", "error");
        }
//        必须要有 collection ,不然只能显示 id
//        save suggest id for future reference
        var suggest = this.config.suggest = suggestId;
        if (suggest) {
            webix.$$(suggest).linkInput(node.firstChild, true);
            webix.event(node.firstChild, "click", webix.bind(this.showPopup, this));
        }
        return node;
    }
}, webix.editors.combo);


////////////////////////////    tag selectize    ////////////////////////////

webix.editors.$popup.select = {
    view: "popup", width: 300, height: 400,
    body: '<select id="selectize"><option value="1">a</option><option value="2">b</option></select>',
    on: {
        onShow: function () {
            console.log("show..........");
        }
    }
};
webix.editors.$popup.tagSelectize= {
    view: "popup", width: 300, height: 400,
    body: '<select id="tagSelectize"><option value="1">a</option><option value="2">b</option></select>',
    on: {
        onShow: function () {
            console.log("show..........");
        }
    }
};
webix.editors.$popup.categorySelectize= {
    view: "popup", width: 300, height: 400,
    body: '<select id="categorySelectize"><option value="1">a</option><option value="2">b</option></select>',
    on: {
        onShow: function () {
            console.log("show..........");
        }
    }
};
webix.editors.$popup.classifiedSelectize= {
    view: "popup", width: 300, height: 400,
    body: '<select id="classifiedSelectize"><option value="1">a</option><option value="2">b</option></select>',
    on: {
        onShow: function () {
            console.log("show..........");
        }
    }
};

var count = 1;
webix.editors.selectizePopup = webix.extend({
    focus: function () {
        this.config.selectize.focus();
    },
    getValue: function () {
        var items = this.config.selectize.getValue();
        this.config.selectize.clear();
//        this.config.selectize.options = {};
        var result = JSON.stringify(items);
        console.log("get value : " +result);
        return result || "";
    },
    setValue: function (value) {
        console.log("set value : " + value);
        console.log(this);
        this.getPopup().show(this.node);
        if (this.config.selectize == null) {
            this.createSelectize();
        }
        this.config.selectize.clear();
        var tagIdList = JSON.parse(value);
        if (tagIdList) {
            for (var i = 0; i < tagIdList.length; i++) {
                var tagId = tagIdList[i];
                this.selectizeAddItem(tagId);
            }
        }
    },
    createSelectize: function () {
    },
    selectizeAddItem: function (id) {
        this.config.selectize.addItem(id);
    },
    createPopup: function () {
        this.config.createId = count ++;
        if (this.config.popup)
            return webix.$$(this.config.popup);

        var type = webix.editors.$popup[this.popupType];
        if (typeof type != "string") {
            type = webix.editors.$popup[this.popupType] = webix.ui(type);
//            type = webix.ui(type);
            this.popupInit(type);
        }
        console.log(type);
        return type.config.id;
    },
    popupType: "select",
//    selectize: null,
    selectizeTemplate: ""
}, webix.editors.popup);


//var s;

webix.editors.tagPopup = webix.extend({
    selectizeAddUrl: function () {
        return "/c/tags/get/";
    },
    selectizeUrl: function () {
        return "/c/tags/tag/like/";
    },
    createSelectize: function () {
        this.config.selectize = tag($("#tagSelectize"), this.selectizeUrl());
    },
    selectizeAddItem: function (id) {
        this.config.selectize.addItemFromServer(id, this.selectizeAddUrl());
    },
    popupType: "tagSelectize"
//    selectize: null
}, webix.editors.selectizePopup);


webix.editors.categoryPopup = webix.extend({
    selectizeAddUrl: function () {
        return "/c/tags/get/";
    },
    selectizeUrl: function () {
        return "/c/tags/category/like/";
    },
    createSelectize: function () {
        this.config.selectize = tag($("#categorySelectize"), this.selectizeUrl());
    },
    selectizeAddItem: function (id) {
        this.config.selectize.addItemFromServer(id, this.selectizeAddUrl());
    },
    popupType: "categorySelectize"
//    selectize: null
}, webix.editors.selectizePopup);

webix.editors.classifiedPopup = webix.extend({
    selectizeAddUrl: function () {
        return "/c/tags/get/";
    },
    selectizeUrl: function () {
        return "/c/tags/category/like/";
    },
    createSelectize: function () {
        var $classified ;
        var that = this;
        webix.ajax().sync().get("/c/classified/list", {}, function (text) {
            var data = JSON.parse(text);
            console.log(data);
            $classified = $('#classifiedSelectize').selectize({
                delimiter: ',',
                plugins: ['remove_button'],
                maxItems: null,
                options: [],
                create: false,
                valueField: 'id',
                labelField: 'name',
                render: {
                    option: function (item, escape) {
                        return '<div>' +
                            '<span class="value">' + escape(item.name) + '</span>' +
                            '</div>';
                    },
                    item: function (item, escape) {
                        var html = '<div class="item">' + escape(item.name) + '</div>';
                        return html;
                    }
                }
            })[0].selectize;
            $classified.clearOptions(true);
            data.forEach(function (value) {
                $classified.addOption(value)
            });
            that.config.classifiedData = data;
            that.config.selectize = $classified;
        });
    },
    selectizeAddItem: function (id) {
        //this.config.selectize.addItemFromServer(id, this.selectizeAddUrl());
        var data = this.config.classifiedData ;
        var classified = null;
        for (var i = 0; i < data.length; i++) {
            var temp = data[i];
            if (temp.id == id) {
                classified = temp;
                break;
            }
        }
        this.config.selectize.addOption(classified);
        this.config.selectize.addItem(classified.id);
    },
    popupType: "classifiedSelectize"
//    selectize: null
}, webix.editors.selectizePopup);

// options 固定的 editor
webix.editors.limitTagPopup = webix.extend({
    setValue: function (value) {
        console.log("limit set value...");
        console.log(value);
        console.log(this);
        this.getPopup().show(this.node);
        if (this.config.selectize == null) {
            this.createSelectize();
        }
        this.config.selectize.clear();

        var tagIdList = JSON.parse(value);
        if (tagIdList) {
            console.log(tagIdList);
            for (var i = 0; i < tagIdList.length; i++) {
                var tagId = tagIdList[i];
                this.selectizeAddItem(tagId);
            }
        }
    },
    createPopup: function () {
        this.config.createId = count++;
        this.config.selectizeId = "selectize" + this.config.createId;

        if (this.config.popup)
            return webix.$$(this.config.popup);

        var type = webix.ui({
            view: "popup", width: 300, height: 400,
            body: '<select id="' + this.config.selectizeId + '"></select>',
            on: {
                onShow: function () {
                    console.log("show categorySelectize..........");
                }
            }
        });
        console.log(this.config.selectizeId);
        this.popupInit(type);
        return type.config.id;
    },
    createSelectize: function () {
        console.log(this);
        console.log(this.popupType+"  "+ this.row);
        console.log(this.config.labelOptions(this.row));
        this.config.selectize = tag($("#" + this.config.selectizeId + ""),this.config.likeUrl);
    },
    selectizeAddItem: function (id) {
        this.config.selectize.addItemFromServer(id, this.config.getUrl);
    }
//    selectize: null
}, webix.editors.selectizePopup);

webix.editors.labelPopup = webix.extend({
    createSelectize: function () {
        console.log("createLabel popup....");
        console.log(this.config.labelOptions);
        this.config.selectize = localSelectize($("#selectize"), this.config.labelOptions, this.config.labelField);
        console.log(this.config.selectize.getValue());

//        webix.ajax().get("/c/label/list", {}, function (text) {
//            var allLabels = JSON.parse(text);
//        })
    }
}, webix.editors.selectizePopup);













// 自定义上传列表
webix.type(webix.ui.list, {
    name: "myUploader",
    template: "<span style='width: 70px;display: inline-block;float: left'>#name#</span>  {common.removeIcon()}{common.percent()}",
    percent: function (obj) {
        if (obj.status == 'transfer')
            return "<div style='width:60px; text-align:center; float:right'>" + obj.percent + "%</div>";
        return "<div class='webix_upload_" + obj.status + "' style='width: 50%;float:right;margin-right:30px;text-align:left'>" + (obj.status == "error" ? "<span class='error_icon'></span>" : (obj.sname ? "<input type='text' style='width:100%' value='" + obj.sname + "'/>" : "<span class='fa-ok webix_icon'></span>")) + "</div>";
    },
    removeIcon: function (obj) {
        return "<div class='webix_remove_upload'><span class='cancel_icon'></span></div>";
    },
    on_click: {
        "webix_remove_upload": function (ev, id) {
            webix.$$(this.config.uploader).files.remove(id);
        }
    }
});



function preview(html) {
    function isImg(extention) {
        var imgType = ['BMP','JPG','JPEG','PNG','GIF'];
        for (var i = 0; i < imgType.length; i++) {
            var obj = imgType[i];
            if(obj === extention){
                return true;
            }
        }
        return false;
    }
    var reg = new RegExp("\\[\\[([\\w\\d]{32}\\.*\\w*)\\]\\]", "gi");
    return html.replace(reg, function (word) {
        var extention = word.match(/\.\w+\]/ig)[0].slice(1,-1).toLocaleUpperCase();
        var name = word.slice(2,-2);
        if(isImg(extention)) {
            console.log("img...");
            var imgUrl = '';
            $.ajax("/c/img/"+name + "/",{async:false,type:"get",success:function (e) {
                imgUrl = e;
            }});
            return '<img src="' + imgUrl + '" alt="asdf" />';
        } else if(extention === 'TEXT'){
            console.log("text ...");
            var text = '';
            $.ajax("/c/file/"+name + "/",{async:false,type:"get",success:function (e) {
                text = e;
            }});
            return '<p><hr/><br/>'+text + '</p>';
        } else {
            console.log("file...");
            return '<p><hr/><br/><a href="/c/file/'+name+'/">附件下载</a></p>'
        }
    })
}
