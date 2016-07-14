package com.seveniu.spider.imgParse;

import com.seveniu.AppCrawl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.Site;

/**
 * Created by seveniu on 7/13/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppCrawl.class)
@ActiveProfiles("home")
public class HtmlImageProcessTest {
    @Autowired
    HtmlImageProcess htmlImageProcess;
    @Test
    public void process() throws Exception {
        String html = "<div class=\"news_content\" itemprop=\"description\">\n" +
                "                <dl class=\"list_catalogue\" style=\"display: none;\">\n" +
                "                    <dd></dd>\n" +
                "                </dl>\n" +
                "                                <div class=\"article_picwrap\">\n" +
                "                    <a href=\"http://go.smzdm.com/adf8c8570f0c6c2e/ca_aa_ad_163_27752_361_0_0\" class=\"picLeft\" target=\"_blank\">\n" +
                "                        <img src=\"http://a.zdmimg.com/201607/12/578507f7cadb56379.jpg_a200.jpg\" width=\"190px\" height=\"190px\">\n" +
                "                    </a>\n" +
                "                                            <div class=\"buy\">\n" +
                "                                                            <a data-type=\"\" isconvert=\"1\" onclick=\"gtmAddToCart({'name':'《到站秀》第62弹：ZEROTECH 零度智控 DOBBY 口袋无人机','id':'27752' , 'price':'0','brand':'ZEROTECH/零度智控' ,'mall':'天猫精选', 'category':'电脑数码/智能设备/无人机/无','metric1':'0','dimension10':'smzdm.com','dimension9':'news','dimension11':'无','dimension20':'无','dimension25':'361'});if(typeof change_direct_url != 'undefined' &amp;&amp; change_direct_url instanceof Function){change_direct_url(this)}\" href=\"http://go.smzdm.com/adf8c8570f0c6c2e/ca_aa_ad_163_27752_361_0_0\" target=\"_blank\" rel=\"nofollow\">直达链接<i class=\"icon-down\"></i></a>\n" +
                "                                                                                        <ul class=\"more_buy\">\n" +
                "                                                                            <li>\n" +
                "                                                                                            <a onclick=\"gtmAddToCart({'name':'《到站秀》第62弹：ZEROTECH 零度智控 DOBBY 口袋无人机','id':'27752' , 'price':'0','brand':'ZEROTECH/零度智控' ,'mall':'天猫精选', 'category':'电脑数码/智能设备/无人机/无','metric1':'0','dimension10':'smzdm.com','dimension9':'news','dimension11':'无','dimension20':'无','dimension25':'361'});if(typeof change_direct_url != 'undefined' &amp;&amp; change_direct_url instanceof Function){change_direct_url(this)}\" href=\"http://news.smzdm.com/tag/%E5%88%B0%E7%AB%99%E7%A7%80/\" rel=\"nofollow\" target=\"_blank\">往期回顾</a>\n" +
                "                                                                                    </li>\n" +
                "                                                                    </ul>\n" +
                "                                                    </div>\n" +
                "                                    </div>\n" +
                "                                <p itemprop=\"description\">最近两年，无人机可以说是正式从专业领域走进了消费领域，入门的产品甚至已经降到与智能手机产品相当的价格。虽然人们对于这类产品的依赖性远远不如智能手机，但其“上帝视角”是一种全新的体验，不少人因此从好奇者变成了无人机拥有者。</p>\n" +
                "<p itemprop=\"description\">在这类带有一定危险的飞行器上，传统的思路是做一些简单的加法：用尽量简单的电路和元器件来保证可靠性，然后将各个电路连接起来。然而不管是汽车还是无人机，都是朝着同样的方向发展的，智能化、集成化、小型化是不可逆转的趋势。近来我们已经能够在各家产品上看到集成化程度愈发高的结构，上游厂商也正在不断推出一体式的方案，比如高通的Flignt。这个方案将骁龙801这种SoC用在了无人机上，并充分地利用了其内部的Hexagon DSP、Wi-Fi、ISP、GPS组件。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/05/577b994a5a4b39258.jpg_e600.jpg\" _size=\"undefined\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">不止高通想将集成度极高的SoC用在无人机上，一家来自中国的无人机企业ZEROTECH（零度智控），也看到了集成化方案的的前景，并将之付诸实践。和高通Flight一样，零度智控为自家产品打造的核心系统也是基于骁龙801芯片，不过融合了自家更成熟的飞控技术和计算机视觉。成果就是今天《到站秀》的主角：DOBBY口袋无人机。</p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY是一架体积迷你的口袋无人机，折叠式设计，在折叠状态下和一台常见的智能手机的大小差不多。它的定位也很明确，不和更大体格的同类们争抢专业级别市场，而是在低空、在室内、在平常的朋友出游中实现其自身价值。“口袋自拍无人机”的说法很有针对性，体积小到可以放进口袋，功能则主要是自拍，而非拍摄大好河山，或者享受视距外高速飞行的快感。目前DOBBY口袋自拍无人机已经上架<a href=\"https://detail.tmall.com/item.htm?id=534623655601\" target=\"_blank\" isconvert=\"1\" data-url=\"http://go.smzdm.com/adf8c8570f0c6c2e/ca_aa_ad_163_27752_0_0_0\" rel=\"nofollow\" onclick=\";if(typeof change_direct_url != 'undefined' &amp;&amp; change_direct_url instanceof Function){change_direct_url(this)}\">zerotech零度智控旗舰店</a>，售价2399元。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f19b43b216238.jpg_e600.jpg\" _size=\"undefined\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;在DOBBY上市之初，我们拿到了这台手机大小的无人机，在这里以到站秀的方式与大家分享一下产品的设计和功能。DOBBY口袋无人机的的包装盒和时下智能手机的包装盒有点像，顶部是品牌名，一侧是产品名，另一侧则有产品的渲染图。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2bb174036383.jpg_e600.jpg\" _size=\"1263680\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2b929f8f9454.jpg_e600.jpg\" _size=\"1685654\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2b7340da489.jpg_e600.jpg\" _size=\"1076164\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;在生产标签上我们看到了比亚迪的身影，DOBBY的电池应该是由比亚迪代工的。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2b476e47744.jpg_e600.jpg\" _size=\"1092048\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2b2c28301208.jpg_e600.jpg\" _size=\"1323190\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;包装盒走简洁路线，内部同样如此。DOBBY口袋无人机通体米白色，表面经过亚光处理，看起来会有些磨砂的质感，摸起来则相对平滑，亚光设计一方面避免了强反射，另一方面则加强了机身表面的摩擦。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2b16ce5f8404.jpg_e600.jpg\" _size=\"1444066\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2af5be566074.jpg_e600.jpg\" _size=\"2068911\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2ad275ab2099.jpg_e600.jpg\" _size=\"1947247\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;内部的主要部件一览，下图从左至右分别是说明书、无人机本体以及配件。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2aac5a5f670.jpg_e600.jpg\" _size=\"1762815\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;包装中的配件包括充电器、电池充电器、电池、USB Type-C数据线以及USB Type-C转Micro USB Type-B的转接头。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f29cc53fa2297.jpg_e600.jpg\" _size=\"1800965\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;电池容量7.37Wh，或者说970mAh，标称电压7.6V，限制充电电压为8.7V。顶部有一个小小的按钮，轻轻按下，就能够看到电池的电量情况，四个灯每个代表25%的电量。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2993a17b939.jpg_e600.jpg\" _size=\"1376825\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f297a84e05877.jpg_e600.jpg\" _size=\"1749880\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;快充充电器，支持5V/2A和9V/2A输出，最大输出功率18W，实测大部分情况下半个小时出头就能够充满一块电池。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f294d6c2a2002.jpg_e600.jpg\" _size=\"1313355\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f293460d92238.jpg_e600.jpg\" _size=\"1311384\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;电池座充和转接头，零度智控直接使用了USB Type-C接口的充电器，迎接未来的主流标准，同时通过提供转接头的方式保持了向下兼容。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f291c77755307.jpg_e600.jpg\" _size=\"1181232\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f28e65c3b9311.jpg_e600.jpg\" _size=\"1230200\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;电池很轻，和手机可换电池时代的一块手机电池差不多。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f28ce7cf05176.jpg_e600.jpg\" _size=\"1365649\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f28a26ddb2989.jpg_e600.jpg\" _size=\"1384513\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f288158f28991.jpg_e600.jpg\" _size=\"1941168\" alt=\"\" title=\"\" _hover-ignore=\"1\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;接下来我们将目光放到无人机本体上，DOBBY含电池整重199g，和大屏手机相当。下图为无人机前部的摄像头，1300万像素，照片最大分辨率4208×3120，支持连拍、4K@30fps和1080P@30fps（稳像状态下）视频拍摄、延时拍摄，以4K采集。内部传感器是来自索尼的IMX214，1/3.06英寸传感器，镜头FOV为75°，等效全幅相机28mm焦距，光圈f/2.2，无穷远处合焦。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2857ea4d6486.jpg_e600.jpg\" _size=\"1381892\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f28239c222116.jpg_e600.jpg\" _size=\"2171348\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;和经常出镜的Nexus 5X对比，这样DOBBY的大小就十分明显了。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f27e87dea8911.jpg_e600.jpg\" _size=\"2151232\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f27a594df2118.jpg_e600.jpg\" _size=\"2700143\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;摄像头没有电机驱动，所以每次起飞之前，需要根据拍摄对象的不同，人工调节摄像头角度，调整范围为-90°~22.5°。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2769e0e56337.jpg_e600.jpg\" _size=\"1328991\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f25cea6bb3052.jpg_e600.jpg\" _size=\"1161785\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f25ad76ce6746.jpg_e600.jpg\" _size=\"1235072\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;电池安装位置在底部，触点对齐后轻轻压下即可。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f25334b014368.jpg_e600.jpg\" _size=\"2450346\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;电池仓旁边，是DOBBY的光流和声纳定位模块，包括一个超声波收发组件和一个底部摄像头，低空和室内的悬停、以及掌上降落主要靠光流定位系统来判断。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2505ff272782.jpg_e600.jpg\" _size=\"1367978\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f24d9d5994924.jpg_e600.jpg\" _size=\"1670736\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;旋翼展开的情况下体积稍大一些。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f23b80db44248.jpg_e600.jpg\" _size=\"2542146\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2396338f9305.jpg_e600.jpg\" _size=\"2215251\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;旋翼为可更换式设计，从上方的结构来看，是可以拆卸和更换的。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f237ca9b76715.jpg_e600.jpg\" _size=\"1671387\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2364f1a86363.jpg_e600.jpg\" _size=\"1535423\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f235043668940.jpg_e600.jpg\" _size=\"1558597\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;真正的掌上无人机，不仅可以放进口袋、捧在手心，还能轻松地完成掌上起飞和降落动作。8000Hz的飞控采样频率，“双子星”双备份技术，可以应对一些突发情况以保护人身安全。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f233b47a11434.jpg_e600.jpg\" _size=\"1176576\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;电源按键放置在顶部，长按2秒开机，长按3秒关机，正常状态下尾部LED灯常亮蓝灯。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2323f10f6431.jpg_e600.jpg\" _size=\"1637359\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f2310f683793.jpg_e600.jpg\" _size=\"1351471\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY口袋自拍无人机使用智能手机来操控，暂时还没有专用遥控。通过官方的“Do.Fun”应用以Wi-Fi连接至DOBBY，就能获取图传信号。DOBBY的图传质量可选640×480@30fps、320×240@30fps或720P@30fps，延时为160ms，人体能够感受到，所以最好不要只看图传来操控无人机，以肉眼观察方向和速度是最靠谱的。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f22f981878242.jpg_e600.jpg\" _size=\"1444189\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f22e36f6e5454.jpg_e600.jpg\" _size=\"1350854\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY支持2.4GHz和5.8GHz双频段Wi-Fi，可以在APP中切换。目前零度智控开发了iOS和Android两个平台的Do.Fun应用，人脸识别、跟随等功能还在逐渐加入中。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f22a83fa18781.jpg_e600.jpg\" _size=\"2015057\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f227e6fb06690.jpg_e600.jpg\" _size=\"1689367\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f21513b5e5440.jpg_e600.jpg\" _size=\"1728455\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY使用了GPS + GLONASS双微星定位系统，室外需要搜到7颗星才能起飞。室内则无需GPS信号，但是帐号要升级到较高级别才能在室内起飞，APP中的说明是每飞行10分钟升一级。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f213844304280.jpg_e600.jpg\" _size=\"1178983\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f211e7c169538.jpg_e600.jpg\" _size=\"1144889\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f20d5a1848493.jpg_e600.jpg\" _size=\"1085268\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY飞行时最大可承受四级风速，内置三轴电子防抖系统，可以抵消飞行时抖动给画面带来的影响，这可以一定程度弥补DOBBY没有云台的缺点。但小编从前几张拍摄的照片来看，真的需要一段时间的练习才能掌握DOBBY拍摄的技巧，不然还是有照片糊掉的风险。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f20be80b39032.jpg_e600.jpg\" _size=\"1281001\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f20ad87d74763.jpg_e600.jpg\" _size=\"1042754\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;虽然做到了小体积、便携，但受制于此，DOBBY的续航时间也成为了最大的一个缺憾：续航时间仅9分钟。正常模式下飞行，尾部蓝灯常亮，点亮不足时则会变成红色，此时手机APP也会不断震动提醒用户返航。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f207c71d97394.jpg_e600.jpg\" _size=\"986833\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1e7d35aa9677.jpg_e600.jpg\" _size=\"942040\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1e482bd78751.jpg_e600.jpg\" _size=\"1052690\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY的控制范围为100m，飞行限高50m，所以就不要想视距外控制了，手机的Wi-Fi天线和DOBBY的体积制约了信号强度，同时因为无人机体积过小，太远之后肉眼已经不好寻找，容易“放生”。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1d092af81177.jpg_e600.jpg\" _size=\"1239817\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1cc32c928068.jpg_e600.jpg\" _size=\"1154747\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;室内模式下，DOBBY使用光流+超声波来定位，该状态下最大上升高度3m，但光流对环境光亮度有一定的要求，照度需要大于15lux，光线过强时也会对其有一定的影响。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1ca5d1ea8600.jpg_e600.jpg\" _size=\"1289687\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1c9083d68692.jpg_e600.jpg\" _size=\"1367957\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1c7130c92099.jpg_e600.jpg\" _size=\"1835623\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1c52acce2515.jpg_e600.jpg\" _size=\"1448595\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY光流定位情况下悬停精度为垂直方向+/- 0.1米，水平方向+/- 0.3米；室外则是垂直方向+/- 0.5米，水平+/- 1.0米。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1c38ee026348.jpg_e600.jpg\" _size=\"922850\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1b8d27433075.jpg_e600.jpg\" _size=\"1334189\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1ae1e2cb1818.jpg_e600.jpg\" _size=\"1535957\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;功能方面，零度智控还会在后期的固件和APP更新中带来更多的功能玩法，包括人脸识别和目标跟随，届时DOBBY将可以锁定目标，并跟随其运动，适合用来拍摄自驾、骑行、跑步等场景。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1a6702a98733.jpg_e600.jpg\" _size=\"1231190\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1a25a9b73083.jpg_e600.jpg\" _size=\"1304078\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;在APP上按下掌上降落之后，将手移到DOBBY的下方，挡住光流模块，它就会缓慢降落在手掌上并停转。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f1a0573e86857.jpg_e600.jpg\" _size=\"1138562\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f19e26b888268.jpg_e600.jpg\" _size=\"1083092\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f18bd6e8b4866.jpg_e600.jpg\" _size=\"1069544\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">从操控性上来说，DOBBY无法和专业向的无人机相比，手机触屏操控的体验远远低于专用遥控的手感和精确性，所以还是用它专职娱乐比较好。至于大家都很关心的禁飞问题，零度智控的说法是按照官方规定来，北京五环内禁飞，机场附近禁飞，其他城市市区相对宽松一些。</p>\n" +
                "<p itemprop=\"description\">▼&nbsp;下面我们来看看遥控APP的功能，DOBBY使用手机触控操作，左侧上下滑动是上升和下降，在屏幕左侧左右滑动则是让无人机左右旋转；右侧的那个小白点则代表着体感操作区，用手按住屏幕靠右的区域，然后倾斜手机，前倾时DOBBY会向无人机前方加速、后倾时后退、左倾时向左运动，依此类推。因为存在一定的延时，体感操作也存在一定的滞后，小区域的控制需要一定功力。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501b7de42d1892.jpg_e600.jpg\" _size=\"1153705\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;右侧从上至下的按钮分别是：设置、定时拍照、拍照模式、快门、视频拍摄按键以及相册查看按键。左侧从上至下则是账户详情、掌上起飞、定点起飞和语音操作。按住语音符号，喊出“DOBBY”就是起飞，喊“回家”则是降落。DOBBY支持连拍，最多一次连拍15张，还能拍摄短视频，在Do.Fun应用中可以直接将图片和短视频分享给社交网络上的好友。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501bba8332148.jpg_e600.jpg\" _size=\"1083614\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼ 拍摄参数调节里边，可以手动调节白平衡和EV值。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501bcd6e3f8448.jpg_e600.jpg\" _size=\"1063873\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501be06d5b5677.jpg_e600.jpg\" _size=\"1053882\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501bf01aee6794.jpg_e600.jpg\" _size=\"1063620\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c0244f23963.jpg_e600.jpg\" _size=\"1021951\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;稳像模式主要用来拍摄视频，该模式下无人机的飞行会更加平稳。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c10c14b313.jpg_e600.jpg\" _size=\"1024734\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c2135a81587.jpg_e600.jpg\" _size=\"1129217\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼&nbsp;DOBBY的相册里，点开一张照片，该照片就会自动缓存至手机当中，也可以批量下载或者批量删除。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c5577c42187.jpg_e600.jpg\" _size=\"1213577\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼ 室内实拍样张，张大妈的休息区域。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c80d7ef4899.jpg_e600.jpg\" _size=\"2289679\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c67963c1354.jpg_e600.jpg\" _size=\"1598831\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼ 室外，北京的雾霾天拍摄，某小区。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501cfbb0141478.jpg_e600.jpg\" _size=\"2611617\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501cdeaf0e173.jpg_e600.jpg\" _size=\"2601100\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501cbf052f2797.jpg_e600.jpg\" _size=\"2534767\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501d19c09c5211.jpg_e600.jpg\" _size=\"3060263\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">▼ 夜拍，笼罩在迷雾中的小编足球队……</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/12/578501c9d9d9b2858.jpg_e600.jpg\" _size=\"4043193\" alt=\"\" title=\"\"></span></p>\n" +
                "<p itemprop=\"description\">本期《到站秀》“DOBBY口袋自拍无人机”到这里就告一段落了，感谢大家的耐心阅读。DOBBY诚然不是完美的无人机产品，但它从一个不同的角度切入无人机市场，本就令人眼前一亮。以拍摄为主打功能而非飞行，洞悉了目前的市场需求。试想和朋友一起外出游玩，想拍一张合影且让所有人都入镜，附近却没有固定手机和相机的物体，无法用它们定时拍摄。这时DOBBY就能派上用场了，如同上图中的场景：和朋友踢完球之后合影留念，无需其他人帮忙就可以让所有人都入镜，而且可以仰拍、俯拍。所以对于部分人群，DOBBY这样的产品还是有一定市场的，它能带来一些其他产品无法做到的乐趣和方便。</p>\n" +
                "<p itemprop=\"description\"><span class=\"img_desc\"><img src=\"http://a.zdmimg.com/201607/10/5781f22c891064180.jpg_e600.jpg\" _size=\"2140570\" alt=\"\" title=\"\"></span></p>\n" +
                "<blockquote><p itemprop=\"description\">关于《到站秀》：<br>什么值得买（SMZDM.COM）作为引领趋势的消费决策平台，在收到网友投递UGC内容的同时，也经常收到厂商送来的先行体验产品，并会自行采买各种新锐科技产品。不论是出于对产品的极端热爱还是对新技术的浓厚兴趣，都有必要让广大值友们在第一时间看到。而为了和主流媒体有所区别，张大妈这里选择了<a href=\"http://www.smzdm.com/tag/%E5%88%B0%E7%AB%99%E7%A7%80/news\" target=\"_blank\">到站秀</a>的栏目形式，用一种轻量平实的表达方式，在资讯频道给大家带来第一手的直观体验。</p>\n" +
                "<ul class=\" list-paddingleft-2\" style=\"list-style-type: disc;\">\n" +
                "<li>\n" +
                "<p itemprop=\"description\">和晒物比，这里没有个人观点，只是最速的介绍；</p>\n" +
                "</li>\n" +
                "<li>\n" +
                "<p itemprop=\"description\">和评测比，这里没有细节测试，只是客观的展示；</p>\n" +
                "</li>\n" +
                "<li>\n" +
                "<p itemprop=\"description\">和广告比，这里没有华丽辞藻，只是简洁的叙述。</p>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</blockquote>\n" +
                "                </div>";
        htmlImageProcess.process("",html,new Site());
    }

}