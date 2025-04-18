package com.pygeton.nibot.communication.function;

import com.pygeton.nibot.communication.entity.Message;
import com.pygeton.nibot.communication.entity.params.SendMsgParams;
import com.pygeton.nibot.communication.event.IMessageEvent;
import com.pygeton.nibot.repository.service.AdminDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Help extends Function implements IMessageEvent {

    @Autowired
    AdminDataServiceImpl adminDataService;

    @Override
    public int weight() {
        return 101;
    }

    @Override
    public boolean onMessage(Message message) {
        setRawMessage(message);
        if(rawMessage[0].equals("/help")){
            sendMsgParams = new SendMsgParams(message);
            String text;
            if(rawMessage.length > 1){
                text = match(message,Integer.parseInt(rawMessage[1]));
            }
            else {
                text = match(message,0);
            }
            sendMsgParams.addTextMessageSegment(text);
            sendMessage();
            return true;
        }
        else return false;
    }

    private String match(Message message,int param){
        /*if (adminDataService.isAdminExist(message.getUserId())){
            switch (param){
                case -1 -> {
                    return """
                           【管理员限定功能】
                           1./admin add [参数]
                           【超级管理员限定】参数内为待添加的管理员QQ号，能够赋予其管理员权限。
                           2./admin del [参数]
                           【超级管理员限定】参数内为待撤销的管理员QQ号，能够撤销其管理员权限。
                           3./admin list
                           能够查看当前系统中的所有管理员和其权限。
                            """;
                }
                case 1 -> {
                    if(rawMessage.length == 2){
                        return """
                        以下是支持的功能列表：
                        1.b50 查询Best50
                        2.info 查看歌曲谱面信息
                        3.search 查询歌曲
                        4.add 为歌曲添加别名
                        5.line 误差分析和分数线计算
                        6.plate 查询牌子进度
                        7.rec 上分歌曲推荐
                        8.status 获取服务器状态
                        9.ds 查询歌曲定数表
                        10.list 查询分数列表
                        可以输入“/help 1 [序号]”查看某项功能的具体使用方法
                        例如：输入/help 1 2，可以查看“查看歌曲谱面信息”的功能详情。
                        """;
                    }
                    else if(rawMessage.length == 3){
                        switch (Integer.parseInt(rawMessage[2])){
                            case 1 -> {
                                return """
                                    使用方法：
                                    /mai b50
                                    可以生成一张带有你的b50信息的图片，前提是你的qq号已经绑定到水鱼查分器上。如未绑定，可以前往https://www.diving-fish.com/maimaidx/prober进行绑定。
                                    如果提示该用户禁止他人访问获取数据，大概率是由于查分器网站用户协议更新未同意造成，可登录上述网站同意用户协议解决。
                                    分数后三位全部为0的，是由于查分器开启了查询时使用掩码造成，同样可登录上述网站打开个人资料，将使用掩码的勾选框取消即可解决。
                                    """;
                            }
                            case 2 -> {
                                return """
                                    使用方法：
                                    /mai info [参数1] [参数2(可选)]
                                    参数1内填写歌曲的谱面id，参数2内填写谱面的难度(绿/黄/红/紫/白)可以查看这个谱面和对应歌曲的详细信息，如果不填写参数2将获取歌曲的基本信息。
                                    如：发送“/mai info 10363 紫”可以获取牛奶DX紫谱的详细信息。
                                    """;
                            }
                            case 3 -> {
                                return """
                                    使用方法：
                                    /mai search [参数]
                                    参数内填写歌曲名的关键字(不区分大小写，可以是歌曲名字的一部分或者是歌曲的别名，前提是这个别名在数据库中)，可以通过模糊匹配返回一个带有歌曲名的谱面id列表。
                                    """;
                            }
                            case 4 -> {
                                return """
                                    使用方法：
                                    /mai add [参数1] [参数2]
                                    参数1内填写歌曲的谱面id（如果这首歌同时有标谱和dx谱，两个谱面id皆可），参数2内填写你想要为此歌曲添加的别名，即可为这首歌添加一个别名以方便查找。
                                    """;
                            }
                            case 5 -> {
                                return """
                                    使用方法：
                                    /mai line [参数1] [参数2] [参数3(可选)]
                                    参数1内填写谱面id，参数2内填写谱面的难度(绿/黄/红/紫/白),即可查看该谱面的所有种类note的误差列表。
                                    若在参数3内填写你要达到的目标分数线，还可以查看达到此目标的容错相关计算信息。
                                    """;
                            }
                            case 6 -> {
                                return """
                                    【目前舞代牌子暂时无法查询】
                                    使用方法：
                                    /mai plate [参数]
                                    参数内填写牌子的名称，即可查看该牌子的进度信息，支持查询极/将/神/舞舞牌。
                                    版本为DX及之后的，填写该版本的任一小版本名即可。
                                    如：输入“/mai plate 熊将”，即可查看熊/华将的进度信息。
                                    """;
                            }
                            case 7 -> {
                                return """
                                    使用方法：
                                    /mai rec
                                    可以获取根据你的b50综合歌曲统计数据联合推算出的推荐上分歌曲。由于本游戏个人差较大，统计数据也存在一定偏差，仅供参考。
                                    注意：当玩家的rating达到15500以上时，将不会显示SS+评级的歌曲推荐。除此之外，本功能不会推荐12级以下的歌曲，建议有一定游玩时长后再使用本功能，其分析结果会具备更高的参考价值。
                                    """;
                            }
                            case 8 -> {
                                return """
                                    使用方法：
                                    /mai status
                                    可以查看当前的华立服务器状态。数据来源于https://status.naominet.live/status/wahlap。
                                    """;
                            }
                            case 9 -> {
                                return """
                                    使用方法：
                                    /mai ds [参数]
                                    参数内填写等级即可获取该等级下所有歌曲的定数表。
                                    如：输入“/mai ds 13+”，即可查看等级为13+的所有歌曲的定数表。
                                    注意：此功能仅支持等级为13+及以上歌曲。
                                    """;
                            }
                            case 10 -> {
                                return """
                                    使用方法：
                                    /mai list [参数]
                                    参数内填写等级即可获取该等级下所有歌曲的分数评级表。
                                    如：输入“/mai ds 14”，即可查看等级为14的所有歌曲的定数表。
                                    注意：此功能仅支持等级为13+及以上歌曲。
                                    """;
                            }
                            default -> {
                                return "目前还没有这个功能哦=_=";
                            }
                        }
                    }
                    else return "参数过多识别不了啦！>_<";
                }
                case 2 -> {
                    if(rawMessage.length == 2){
                        return """
                        以下是支持的功能列表：
                        1.b30 查询B30+R10
                        2.info 查看歌曲谱面信息
                        3.search 查询歌曲
                        4.add 为歌曲添加别名
                        5.line 误差分析和分数线计算
                        可以输入“/help 2 [序号]”查看某项功能的具体使用方法
                        例如：输入/help 2 5，可以查看“误差分析和分数线计算”的功能详情。
                        """;
                    }
                    else if(rawMessage.length == 3){
                        switch (Integer.parseInt(rawMessage[2])){
                            case 1 -> {
                                return """
                                    使用方法：
                                    /chu b30
                                    可以生成一张带有你的b30+r10数据的图片，前提是你的qq号已经绑定到水鱼查分器上。如未绑定，可以前往https://www.diving-fish.com/maimaidx/prober进行绑定。
                                    """;
                            }
                            case 2 -> {
                                return """
                                    使用方法：
                                    /chu info [参数1] [参数2(可选)]
                                    参数1内填写歌曲的谱面id，参数2内填写谱面的难度(绿/黄/红/紫/黑)可以查看这个谱面和对应歌曲的详细信息，如果不填写参数2将获取歌曲的基本信息。
                                    同时支持查看日服已实装但是国服未实装的歌曲信息，但是未实装歌曲没有谱面详细信息。
                                    对于WORLD'S END谱面，无需输入参数2，只需输入歌曲id即可。
                                    如：发送“/chu info 2035 紫”可以获取特大紫谱的详细信息，发送“/chu info 2336”可以获取盟月的歌曲信息，发送“/chu info 8124”可以获取火山WE谱面的详细信息。
                                    """;
                            }
                            case 3 -> {
                                return """
                                    使用方法：
                                    /chu search [参数]
                                    参数内填写歌曲名的关键字(不区分大小写，可以是歌曲名字的一部分或者是歌曲的别名，前提是这个别名在数据库中)，可以通过模糊匹配返回一个带有歌曲名的谱面id列表。
                                    搜索结果可能包括国服未实装的歌曲，以(*)作标记。
                                    """;
                            }
                            case 4 -> {
                                return """
                                    使用方法：
                                    /chu add [参数1] [参数2]
                                    参数1内填写歌曲的谱面id，参数2内填写你想要为此歌曲添加的别名，即可为这首歌添加一个别名以方便查找。
                                    """;
                            }
                            case 5 -> {
                                return """
                                    使用方法：
                                    /chu line [参数1] [参数2] [参数3(可选)] [参数4(可选)]
                                    参数1内填写谱面id，参数2内填写谱面的难度(绿/黄/红/紫/黑),即可查看该谱面的误差列表。
                                    若在参数3内填写你要达到的目标分数线(支持1000-1010或10000-10100的简写)或是目标评级(支持ss-sss+/鸟/鸟加)，还可以查看达到此目标的容错相关计算信息。
                                    若在参数4内填写你的预期Justice(小J)数量，还可以查看在你预期的Justice误差下，达到目标的Attack和Miss容错数量。(如果不填此参数默认预期Justice数量为0)
                                    如：输入“/chu line 981 紫 sss”可以查看小恶魔紫谱的达到1007500分数线的容错计算，输入“/chu line 1035 紫 1006”可以查看赤壁大炎上的达到1006000分数线的容错计算。
                                    注意：此功能无法对国服未实装的歌曲进行误差计算。
                                    """;
                            }
                            default -> {
                                return "目前还没有这个功能哦=_=";
                            }
                        }
                    }
                    else return "参数过多识别不了啦！>_<";
                }
                default -> {
                    return """
                        NiBot现在支持的功能如下：
                        1/6./mai 舞萌DX
                        2/7./chu 中二节奏
                        可以通过输入“/help [序号]”查看某项功能的具体使用方法或是细分的功能列表
                        例如：输入/help 1，可以查看“舞萌DX”的功能详情。
                        """;
                }
            }
        }
        else {*/
            switch (param){
                case 1 -> {
                    return """
                        使用方法：
                        /luck [参数(可选)]
                        直接输入/luck即可查询自己的今日运势，在参数内@群友就可以查询ta的今日运势，每日运势值固定。
                        """;
                }
                case 2 -> {
                    return """
                        使用方法：
                        /choose [参数1] [参数2]...[参数n]
                        每个参数之间以空格分割，发送后镍酱就会帮你做出选择！
                        """;
                }
                case 3 -> {
                    return """
                        使用方法：
                        1.绑定公式战账号
                        /mj bind [参数1] [参数2(可选)]
                        参数1内填写公式战的登记名称即可完成绑定，若提示错误并显示多重结果，请根据信息在参数2内填写符合自己所在区域的区域编号。后续无需绑定。
                        2.查询战绩
                        /mj rate [参数(可选)]
                        参数内@其他群友即可查询其战绩（前提是已经绑定），不填写参数则是查询自己的战绩。
                        """;
                }
                case 4 -> {
                    if(message.getGroupId() != null){
                        if(message.getGroupId() == 653948081L)
                        {
                            return """
                        使用方法：
                        1.召唤随机龙图
                        /long
                        可以召唤一张随机的龙图.jpg
                        2.添加龙图
                        [图片]+回复：/long add
                        发送一张你想要添加的龙图，然后对龙图回复并输入上述指令，审核通过后你贡献的龙图就会出现在随机池里！
                        【审核群限定】其他群发出的龙图添加请求会被转发到此群，若认定通过审核，直接回复该龙图并输入上述指令即可将此龙图加入随机召唤池。同理，在本群直接发起添加操作无需审核，视为直接通过。
                        """;
                        }
                        else {
                            return """
                        使用方法：
                        1.召唤随机龙图
                        /long
                        可以召唤一张随机的龙图.jpg
                        2.添加龙图
                        [图片]+回复：/long add
                        发送一张你想要添加的龙图，然后对龙图回复并输入上述指令，审核通过后你贡献的龙图就会出现在随机池里！
                        """;
                        }
                    }
                    else {
                        return """
                        使用方法：
                        1.召唤随机龙图
                        /long
                        可以召唤一张随机的龙图.jpg
                        2.添加龙图
                        [图片]+回复：/long add
                        发送一张你想要添加的龙图，然后对龙图回复并输入上述指令，审核通过后你贡献的龙图就会出现在随机池里！
                        """;
                    }
                }
                case 5 -> {
                    return """
                        使用方法：
                        /sleep [参数]
                        参数内填写你想要睡眠的时长，单位为时（例如：/sleep 8 = 禁言8小时），发送指令后镍酱就会不让你说话直到你睡醒为止！
                        如果你提前睡醒想说话，需要私聊其他管理员（
                        """;
                }
                case 6 -> {
                    if(rawMessage.length == 2){
                        return """
                        以下是支持的功能列表：
                        1.b50 查询Best50
                        2.info 查看歌曲谱面信息
                        3.search 查询歌曲
                        4.add 为歌曲添加别名
                        5.line 误差分析和分数线计算
                        6.plate 查询牌子进度
                        7.rec 上分歌曲推荐
                        8.status 获取服务器状态
                        9.ds 查询歌曲定数表
                        10.list 查询分数列表
                        11.random 随机歌曲
                        可以输入“/help 6 [序号]”查看某项功能的具体使用方法QAQ
                        例如：输入/help 6 2，可以查看“查看歌曲谱面信息”的功能详情。
                        """;
                    }
                    else if(rawMessage.length == 3){
                        switch (Integer.parseInt(rawMessage[2])){
                            case 1 -> {
                                return """
                                    使用方法：
                                    /mai b50
                                    可以生成一张带有你的b50信息的图片，前提是你的qq号已经绑定到水鱼查分器上。如未绑定，可以前往https://www.diving-fish.com/maimaidx/prober进行绑定。
                                    如果提示该用户禁止他人访问获取数据，大概率是由于查分器网站用户协议更新未同意造成，可登录上述网站同意用户协议解决。
                                    分数后三位全部为0的，是由于查分器开启了查询时使用掩码造成，同样可登录上述网站打开个人资料，将使用掩码的勾选框取消即可解决。
                                    """;
                            }
                            case 2 -> {
                                return """
                                    使用方法：
                                    /mai info [参数1] [参数2(可选)]
                                    参数1内填写歌曲的谱面id，参数2内填写谱面的难度(绿/黄/红/紫/白)可以查看这个谱面和对应歌曲的详细信息，如果不填写参数2将获取歌曲的基本信息。
                                    如：发送“/mai info 10363 紫”可以获取牛奶DX紫谱的详细信息。
                                    """;
                            }
                            case 3 -> {
                                return """
                                    使用方法：
                                    /mai search [参数]
                                    参数内填写歌曲名的关键字(不区分大小写，可以是歌曲名字的一部分或者是歌曲的别名，前提是这个别名在数据库中)，可以通过模糊匹配返回一个带有歌曲名的谱面id列表。
                                    """;
                            }
                            case 4 -> {
                                return """
                                    使用方法：
                                    /mai add [参数1] [参数2]
                                    参数1内填写歌曲的谱面id（如果这首歌同时有标谱和dx谱，两个谱面id皆可），参数2内填写你想要为此歌曲添加的别名，即可为这首歌添加一个别名以方便查找。
                                    """;
                            }
                            case 5 -> {
                                return """
                                    使用方法：
                                    /mai line [参数1] [参数2] [参数3(可选)]
                                    参数1内填写谱面id，参数2内填写谱面的难度(绿/黄/红/紫/白),即可查看该谱面的所有种类note的误差列表。
                                    若在参数3内填写你要达到的目标分数线，还可以查看达到此目标的容错相关计算信息。
                                    """;
                            }
                            case 6 -> {
                                return """
                                    【目前舞代牌子暂时无法查询】
                                    使用方法：
                                    /mai plate [参数]
                                    参数内填写牌子的名称，即可查看该牌子的进度信息，支持查询极/将/神/舞舞牌。
                                    版本为DX及之后的，填写该版本的任一小版本名即可。
                                    如：输入“/mai plate 熊将”，即可查看熊/华将的进度信息。
                                    """;
                            }
                            case 7 -> {
                                return """
                                    使用方法：
                                    /mai rec
                                    可以获取根据你的b50综合歌曲统计数据联合推算出的推荐上分歌曲。由于本游戏个人差较大，统计数据也存在一定偏差，仅供参考。
                                    注意：当玩家的rating达到15500以上时，将不会显示SS+评级的歌曲推荐。除此之外，本功能不会推荐12级以下的歌曲，建议有一定游玩时长后再使用本功能，其分析结果会具备更高的参考价值。
                                    """;
                            }
                            case 8 -> {
                                return """
                                    使用方法：
                                    /mai status
                                    可以查看当前的华立服务器状态。数据来源于https://status.naominet.live/status/wahlap。
                                    """;
                            }
                            case 9 -> {
                                return """
                                    使用方法：
                                    /mai ds [参数]
                                    参数内填写等级即可获取该等级下所有歌曲的定数表。
                                    如：输入“/mai ds 13+”，即可查看等级为13+的所有歌曲的定数表。
                                    注意：此功能仅支持等级为13+及以上歌曲。
                                    """;
                            }
                            case 10 -> {
                                return """
                                    使用方法：
                                    /mai list [参数]
                                    参数内填写等级即可获取该等级下所有歌曲的分数评级表。
                                    如：输入“/mai list 14”，即可查看等级为14的所有歌曲的分数评级表。
                                    注意：此功能仅支持等级为13+及以上歌曲。
                                    """;
                            }
                            case 11 -> {
                                return """
                                    使用方法：
                                    /mai random [参数]
                                    参数内填写等级即可随机一首歌曲。
                                    如：输入“/mai random 13”，即可随机一首等级为13的歌曲。
                                    注意：此功能仅支持红、紫、白难度。
                                    """;
                            }
                            default -> {
                                return "目前还没有这个功能哦=_=";
                            }
                        }
                    }
                    else return "参数过多识别不了啦！>_<";
                }
                case 7 -> {
                    if(rawMessage.length == 2){
                        return """
                        以下是支持的功能列表：
                        1.b30 查询B30+R10
                        2.info 查看歌曲谱面信息
                        3.search 查询歌曲
                        4.add 为歌曲添加别名
                        5.line 误差分析和分数线计算
                        6.random 随机歌曲
                        7.class 随机段位
                        可以输入“/help 7 [序号]”查看某项功能的具体使用方法QAQ
                        例如：输入/help 7 5，可以查看“误差分析和分数线计算”的功能详情。
                        """;
                    }
                    else if(rawMessage.length == 3){
                        switch (Integer.parseInt(rawMessage[2])){
                            case 1 -> {
                                return """
                                    使用方法：
                                    /chu b30 [参数(可选)]
                                    可以生成一张带有你的b30+r10数据的图片，b30和r10的rating数值后的两位数字代表精确到四位小数的后两位，前提是你的qq号已经绑定到水鱼查分器上。如未绑定，可以前往https://www.diving-fish.com/maimaidx/prober进行绑定。
                                    在参数内@群友即可查询ta的b30成绩。
                                    """;
                            }
                            case 2 -> {
                                return """
                                    使用方法：
                                    /chu info [参数1] [参数2(可选)]
                                    参数1内填写歌曲的谱面id，参数2内填写谱面的难度(绿/黄/红/紫/黑)可以查看这个谱面和对应歌曲的详细信息，如果不填写参数2将获取歌曲的基本信息。
                                    同时支持查看日服已实装但是国服未实装的歌曲信息，但是未实装歌曲没有谱面详细信息。
                                    对于WORLD'S END谱面，无需输入参数2，只需输入歌曲id即可。
                                    如：发送“/chu info 2035 紫”可以获取特大紫谱的详细信息，发送“/chu info 2336”可以获取盟月的歌曲信息，发送“/chu info 8124”可以获取火山WE谱面的详细信息。
                                    """;
                            }
                            case 3 -> {
                                return """
                                    使用方法：
                                    /chu search [参数]
                                    参数内填写歌曲名的关键字(不区分大小写，可以是歌曲名字的一部分或者是歌曲的别名，前提是这个别名在数据库中)，可以通过模糊匹配返回一个带有歌曲名的谱面id列表。
                                    搜索结果可能包括国服未实装的歌曲，以(*)作标记。
                                    """;
                            }
                            case 4 -> {
                                return """
                                    使用方法：
                                    /chu add [参数1] [参数2]
                                    参数1内填写歌曲的谱面id，参数2内填写你想要为此歌曲添加的别名，即可为这首歌添加一个别名以方便查找。
                                    """;
                            }
                            case 5 -> {
                                return """
                                    使用方法：
                                    /chu line [参数1] [参数2] [参数3(可选)] [参数4(可选)]
                                    参数1内填写谱面id，参数2内填写谱面的难度(绿/黄/红/紫/黑),即可查看该谱面的误差列表。
                                    若在参数3内填写你要达到的目标分数线(支持1000-1010或10000-10100的简写)或是目标评级(支持ss-sss+/鸟/鸟加)，还可以查看达到此目标的容错相关计算信息。
                                    若在参数4内填写你的预期Justice(小J)数量，还可以查看在你预期的Justice误差下，达到目标的Attack和Miss容错数量。(如果不填此参数默认预期Justice数量为0)
                                    如：输入“/chu line 981 紫 sss”可以查看小恶魔紫谱的达到1007500分数线的容错计算，输入“/chu line 1035 紫 1006”可以查看赤壁大炎上的达到1006000分数线的容错计算。
                                    注意：此功能无法对国服未实装的歌曲进行误差计算。
                                    """;
                            }
                            case 6 -> {
                                return """
                                    使用方法：
                                    /chu random [参数]
                                    参数内填写等级即可随机一首歌曲。
                                    如：输入“/chu random 13”，即可随机一首等级为13的歌曲。
                                    注意：此功能仅支持红、紫、黑难度。
                                    """;
                            }
                            case 7 -> {
                                return """
                                    使用方法：
                                    /chu class [参数]
                                    参数内填写段位即可进行一次指定段位随机选曲。
                                    如：输入“/chu random V”，即可进行随五段位选曲。
                                    注意：此功能仅支持随四(IV/4)、随五(V/5)、随无限（∞/♾️/无限）。
                                    """;
                            }
                            default -> {
                                return "目前还没有这个功能哦=_=";
                            }
                        }
                    }
                    else return "参数过多识别不了啦！>_<";
                }
                default -> {
                    return """
                        NiBot现在支持的功能如下：
                        1./luck 今日运势
                        2./choose 做选择
                        3./mj 雀庄公式战
                        4./long 召唤龙图
                        5./sleep 精致睡眠
                        6./mai 舞萌DX
                        7./chu 中二节奏
                        可以通过输入“/help [序号]”查看某项功能的具体使用方法或是细分的功能列表QAQ
                        例如：输入/help 1，可以查看“今日运势”的功能详情。
                        """;
                }
            }
        //}

    }
}
