package Main;

import Eliza.ElizaMain;
import Picture.Screenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static LispStyle.LispStyle.evaluate;
import static Main.Main.startTime;
import static Roman.IntegerToEnglish.numberToWords;
import static Roman.IntegerToRoman.intToRoman;
import static Roman.RomanToInteger.romanToInt;
import static String.StringExp.decodeString;
import static java.lang.Math.random;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Locality.GROUP;
import static org.telegram.abilitybots.api.objects.Privacy.ADMIN;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class VickyBotA extends AbilityBot {
    static final int maxEliza=1000;
    static ElizaMain[] eliza;
    static Map<Long,Integer> elizaMap;
    static boolean[] isElizaOccupied;
    static final String scriptPathname = "./cfg/ElizaScript.txt";
    static long curTime,timeH,timeM,timeS;
    static List<Message> sstmp;
    static boolean ssmode;
    static Logger logger= LoggerFactory.getLogger(VickyBotA.class);
    public VickyBotA()
    {
        super(Main.token,Main.name);
        eliza=new ElizaMain[maxEliza];
        elizaMap=new HashMap<>();
        isElizaOccupied=new boolean[maxEliza];
    }
    @Override
    public long creatorId()
    {
        return Main.creatorId;
    }

    public Ability about()
    {
        return Ability.builder()
                .name("about")
                .info("Returns the bot environment.")
                .input(0)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->
                {
                    curTime=Instant.now().getEpochSecond();
                    timeH=(curTime- startTime)/3600;
                    timeM=((curTime- startTime)%3600)/60;
                    timeS=(curTime- startTime)%60;
                    silent.send("A bot by Vicky Silviana.\nOS: "
                                +System.getProperty("os.name")
                                +"\nVer: "
                                +System.getProperty("os.version")
                                +"\nArch: "
                                +System.getProperty("os.arch")
                                +"\nJavaVer: "
                                +System.getProperty("java.version")
                                +"\nUptime:\n"
                                +timeH+"h"+timeM+"m"+timeS+"s", ctx.chatId());
                })
                .build();

    }
    public Ability shutMe()
    {
        return Ability.builder()
                .name("shutme")
                .info("Shut up yourself.")
                .input(0)
                .locality(GROUP)
                .privacy(PUBLIC)
                .action(ctx->
                {
                    ChatPermissions chatPermShut=new ChatPermissions(
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false);

                    long extratime=100+(long)(random()*200);
                    long unixTimeShut= Instant.now().getEpochSecond()+extratime;
                    RestrictChatMember restrictChatMember=new RestrictChatMember(ctx.chatId().toString(),ctx.user().getId(),chatPermShut,(int)unixTimeShut);
                    silent.execute(restrictChatMember);
                    silent.send(ctx.user().getFirstName()+" has been shut for "+ extratime +"s",ctx.chatId());
                })
                .build();
    }
    public Ability shut()
    {
        return Ability.builder()
                .name("shut")
                .info("Shut up somebody.")
                .input(1)
                .locality(GROUP)
                .privacy(ADMIN)
                .action(ctx->
                {
                    ChatPermissions chatPermShut=new ChatPermissions(
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false);

                    long extratime=100+(long)(random()*200);
                    long unixTimeShut= Instant.now().getEpochSecond()+extratime;
                    RestrictChatMember restrictChatMember=new RestrictChatMember(ctx.chatId().toString(),userIds().get(ctx.firstArg().substring(1).toLowerCase()),chatPermShut,(int)unixTimeShut);
                    silent.execute(restrictChatMember);
                    GetChatMember getChatMember=new GetChatMember(ctx.chatId().toString(),userIds().get(ctx.firstArg().substring(1).toLowerCase()));
                    ChatMember chatMember=silent.execute(getChatMember).get();
                    silent.send(chatMember.getUser().getFirstName()+" has been shut for "+ extratime +"s",ctx.chatId());
                })
                .build();
    }
    public Ability javaCup()
    {
        return Ability.builder()
                .name("java")
                .info("Have a cup of java.")
                .input(0)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->
                        silent.send(ctx.user().getFirstName()+  " thanks for your hard work. Have a cup of java and relax!☕\n" +
                                                                        "This bot is written in java.",ctx.chatId()))
                .build();
    }
    public Ability romanToInteger()
    {
        return Ability.builder()
                .name("r2i")
                .info("Turn roman to integer.")
                .input(1)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->{
                        try{silent.send(Integer.toString(romanToInt(ctx.firstArg().toUpperCase())),ctx.chatId());}
                        catch(Exception e){silent.send("ERR: "+e,ctx.chatId());}
                })
                .build();
    }
    public Ability integerToRoman()
    {
        return Ability.builder()
                .name("i2r")
                .info("Turn integer to roman.")
                .input(1)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->{
                    try{silent.send(intToRoman(Integer.parseInt(ctx.firstArg())),ctx.chatId());}
                    catch(Exception e){silent.send("ERR: "+e,ctx.chatId());}
                })
                .build();
    }
    public Ability integerToEnglish()
    {
        return Ability.builder()
                .name("i2e")
                .info("Turn integer to English.")
                .input(1)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->{
                    try{silent.send(numberToWords(Integer.parseInt(ctx.firstArg())),ctx.chatId());}
                    catch(Exception e){silent.send("ERR: "+e,ctx.chatId());}
                })
                .build();
    }
    public Ability exp()
    {
        return Ability.builder()
                .name("exp")
                .info("Expand string that has ...<integer>[...]... structure.")
                .input(0)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->{
                    if(ctx.arguments().length==0)
                    {
                        silent.send("Example: /exp 3[a2[b]]",ctx.chatId());
                    }
                    else
                    {
                        String msg=ctx.update().getMessage().getText();
                        try
                        {
                            silent.send(decodeString(msg.substring(msg.indexOf(" ")+1)),ctx.chatId());
                        }
                        catch(Exception e)
                        {
                            silent.send("ERR: "+e,ctx.chatId());
                        }
                    }
                })
                .build();
    }
    public Ability eliza()
    {
        return Ability.builder()
                .name("eliza")
                .info("Mimics ELIZA.")
                .input(0)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->
                {
                    String msg0=ctx.update().getMessage().getText();
                    String msg=msg0.substring(msg0.indexOf(" ")+1).toLowerCase();
                    if(ctx.arguments().length==0)
                    {
                        silent.send("This is Eliza, a chat program.\n"+
                                            "Start with Hello.\n"+
                                            "Please close it after usage.\n"+
                                            "No need to initialize at the beginning.\n"+
                                            "Talk with her in a separate chat for better experience.\n"+
                                            "/eliza eliza.init - Initialize.\n"+
                                            "/eliza eliza.close - Close this Eliza.\n"+
                                            "/eliza ... - Talk with her.",ctx.chatId());
                    }
                    else
                    {
                        int firstFree=0;
                        if(elizaMap.get(ctx.chatId())==null)
                        {

                            for(;firstFree<maxEliza;firstFree++)
                            {
                                if(!isElizaOccupied[firstFree])break;
                            }
                            if(firstFree==maxEliza)
                            {
                                silent.send("There are too many Elizas!\n"+
                                                    "Please wait for a while...",ctx.chatId());
                            }
                            else
                            {
                                isElizaOccupied[firstFree]=true;
                                elizaMap.put(ctx.chatId(),firstFree);
                                eliza[firstFree]=new ElizaMain();
                                eliza[firstFree].readScript(true, scriptPathname);
                                silent.send("Generated new Eliza.",ctx.chatId());
                            }
                        }
                        else firstFree=elizaMap.get(ctx.chatId());
                        if(firstFree!=maxEliza)
                        {
                            if(msg.toLowerCase().startsWith("eliza.init"))
                            {
                                eliza[firstFree]=new ElizaMain();
                                eliza[firstFree].readScript(true, scriptPathname);
                                silent.send("Initialized your Eliza.",ctx.chatId());
                            }
                            else if(msg.toLowerCase().startsWith("eliza.close"))
                            {
                                isElizaOccupied[firstFree]=false;
                                elizaMap.remove(ctx.chatId());
                                silent.send("Closed your Eliza.",ctx.chatId());
                            }
                            else
                            {
                                String ans = eliza[firstFree].processInput(msg);
                                silent.send(ans, ctx.chatId());
                            }
                        }
                    }
                })
                .build();
    }
    public Ability lispStyle()
    {
        return Ability.builder()
                .name("lisp")
                .info("Process Lisp-style expression.")
                .input(0)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx->
                {
                    if(ctx.arguments().length==0)silent.send(
                            "Support:\n" +
                                    "\tlet: assign values and calculate.\n" +
                                    "\t\t(let v1 e1 v2 e2 ... expr)\n"+
                                    "\tadd: add two values.\n" +
                                    "\t\t(add x y)\n"+
                                    "\tmult: multiply two values.\n" +
                                    "\t\t(mult x y)\n"+
                                    "Still not completely implemented.",ctx.chatId());
                    else
                    {
                        String msg=ctx.update().getMessage().getText().toLowerCase();
                        try{silent.send(Long.toString(evaluate(msg.substring(msg.indexOf(" ")+1))),ctx.chatId());}
                        catch(Exception e){silent.send("ERR: "+e,ctx.chatId());}
                    }
                })
                .build();
    }
    public Ability screenshot()
    {
        return Ability.builder()
                .name("ss")
                .info("Take a screenshot.")
                .input(0)
                .locality(ALL)
                .privacy(ADMIN)
                .action(ctx->
                {
                    Message reply=ctx.update().getMessage().getReplyToMessage();
                    if(reply==null)
                    {
                        silent.send(
                                "Reply to something to use it.\n" +
                                        "/ss - Capture this only.\n" +
                                        "/ss begin - Capture from this. Use /ss after this.\n" +
                                        "/ss end - Capture up to this.",ctx.chatId());
                    }
                    else
                    {
                        if(ctx.arguments().length==0)
                        {
                            if(ssmode)
                            {
                                sstmp.add(reply);
                            }
                            else
                            {
                                sstmp=new ArrayList<>();
                                sstmp.add(reply);
                                try {
                                    Screenshot.screenshot(sstmp,ctx.chatId());
                                    silent.send("Picture saved. Vicky, check it?",ctx.chatId());
                                } catch (Exception e) {
                                    silent.send("ERR: "+e,ctx.chatId());
                                }
                            }
                        }
                        else if(ctx.firstArg().toLowerCase().startsWith("begin"))
                        {
                            ssmode=true;
                            sstmp=new ArrayList<>();
                            sstmp.add(reply);
                        }
                        else if(ctx.firstArg().toLowerCase().startsWith("end"))
                        {
                            if(!ssmode)sstmp=new ArrayList<>();
                            sstmp.add(reply);
                            ssmode=false;
                            try {
                                Screenshot.screenshot(sstmp,ctx.chatId());
                                silent.send("Picture saved. Vicky, check it?",ctx.chatId());
                            } catch (Exception e) {
                                silent.send("ERR: "+e,ctx.chatId());
                            }
                        }
                        else
                        {
                            silent.send("Unknown argument.",ctx.chatId());
                        }
                    }
                })
                .build();
    }
}