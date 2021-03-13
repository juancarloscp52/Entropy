package me.juancarloscp52.entropy.events;

public class EventsHandler {
// TODO: DELETE.

//    private static final int STARTING_COUNTDOWN_DURATION = 10;
//    EntropySettings settings = EntropyClient.getInstance().settings;
//    MinecraftClient client;
//    PlayerEntity player;
//    List<Event> currentEvents = new ArrayList<>();
//    int eventCountDown = settings.timerDuration;
//    int startingCountDown = STARTING_COUNTDOWN_DURATION;
//    List<Event> nextEvents = new ArrayList<>();
//    public Voting voting;
//
//
//    public EventsHandler (MinecraftClient client,  PlayerEntity player) {
//        this.client=client;
//        this.player=player;
//    }
//
//    public void init(){
//        if(settings.twitchIntegration){
//            voting = new Voting();
//            voting.setIntegration(new TwitchIntegrations());
//            voting.enable();
//            voting.reset();
//            nextEvents = new ArrayList<>();
//            nextEvents.add(getRandomEvent(true));
//            nextEvents.add(getRandomEvent(true));
//            nextEvents.add(getRandomEvent(true));
//        }
//        eventCountDown = settings.timerDuration;
//
//
//    }
//
//    public void render(MatrixStack matrixStack, float tickdelta){
//
//        currentEvents.forEach(event -> {
//            if(!event.hasEnded())
//                event.render(matrixStack, tickdelta);
//        });
//        double time = settings.timerDuration - eventCountDown;
//        int width = client.getWindow().getScaledWidth();
//        DrawableHelper.fill(matrixStack,0,0,width,10, 150<<24);
//        DrawableHelper.fill(matrixStack,0,0,MathHelper.floor(width*(time/ settings.timerDuration)),10, MathHelper.packRgb(70,150,70) + (255 << 24));
//        if(settings.twitchIntegration && nextEvents.size()!=0){
//            DrawableHelper.drawTextWithShadow(matrixStack,client.textRenderer,new TranslatableText("entropy.voting.total", voting.getTotalVotes()),10,20,MathHelper.packRgb(255,255,255));
//            for (int i = 0; i < 4; i++) {
//                renderPollElement(matrixStack,i);
//            }
//        }
//        for (int i=0; i<currentEvents.size();i++){
//            currentEvents.get(i).renderQueueItem(matrixStack,tickdelta, width-200, 20+(i*13));
//        }
//    }
//    public void renderPollElement(MatrixStack matrixStack, int i){
//        if(nextEvents.size()!=3 && i>=3 || i> nextEvents.size())
//            return;
//
//        double ratio = voting.getTotalVotes()>0? (double)voting.getVotes()[i]/voting.getTotalVotes():0;
//        int altOffset = voting.isAlt()? 4:0;
//        DrawableHelper.fill(matrixStack,10,31+(i*18),195+10+45,35+(i*18)+10, MathHelper.packRgb(155,22,217) + 150<<24);
//        DrawableHelper.fill(matrixStack,10,31+(i*18),10+MathHelper.floor((195+45)*ratio),(35+(i*18)+10), MathHelper.packRgb(155,22,217) + (150 << 24));
//        Text percentage = new LiteralText(MathHelper.floor(ratio*100)+" %");
//        DrawableHelper.drawTextWithShadow(matrixStack,client.textRenderer,percentage,195+10+42 - client.textRenderer.getWidth(percentage),34+(i*18),MathHelper.packRgb(255,255,255));
//        DrawableHelper.drawTextWithShadow(matrixStack,client.textRenderer,new LiteralText((1+i+altOffset)+": ").append((i!=3? (new TranslatableText(nextEvents.get(i).getTranslationKey())):new TranslatableText("entropy.voting.randomEvent") )),15,34+(i*18),MathHelper.packRgb(255,255,255));
//    }
//
//
//    public void tick(boolean noNewEvents){
//        if(client.player == null || client.player.isDead())
//            return;
//        if(startingCountDown>=0){
//            if(startingCountDown==0)
//                client.openScreen(new EntropyConfigurationScreen());
//            startingCountDown--;
//            return;
//        }
//        if(eventCountDown ==0){
//            if(currentEvents.size()>3){
//                Event first = currentEvents.get(0);
//                if(first.hasEnded()){
//                    currentEvents.remove(0);
//                }
//            }
//
//            if(!noNewEvents){
//                if(settings.twitchIntegration){
//                    int winner = voting.getWinner();
//                    if(winner==-1 || winner==3){
//                        Event event = getRandomEvent(false);
//                        System.out.println("NEW EVENT: "+event.getTranslationKey());
//                        currentEvents.add(event);
//                    }else{
//                        currentEvents.add(nextEvents.get(winner));
//                        System.out.println("WINNER: " + winner + " " + nextEvents.get(winner));
//                    }
//                    currentEvents.get(currentEvents.size()-1).init();
//                    eventCountDown = settings.timerDuration;
//
//                    nextEvents = new ArrayList<>();
//                    nextEvents.add(getRandomEvent(true));
//                    nextEvents.add(getRandomEvent(true));
//                    nextEvents.add(getRandomEvent(true));
//                    int altOffset = !voting.isAlt()? 4:0;
//                    voting.sendMessage("Current poll:\n[" +
//                            (1+altOffset) +" - "+ I18n.translate(nextEvents.get(0).getTranslationKey())+"]\n["+
//                            (2+altOffset) +" - "+ I18n.translate(nextEvents.get(1).getTranslationKey())+"]\n["+
//                            (3+altOffset) +" - "+ I18n.translate(nextEvents.get(2).getTranslationKey())+"]\n["+
//                            (4+altOffset) +" - "+ I18n.translate("entropy.voting.randomEvent")+"]"
//                    );
//                    voting.reset();
//
//                }else{
//                    Event event = getRandomEvent(false);
//                    System.out.println("NEW EVENT: "+event.getTranslationKey());
//                    currentEvents.add(event);
//                    currentEvents.get(currentEvents.size()-1).init();
//                    eventCountDown = settings.timerDuration;
//                }
//
//            }
//        }
//
//        for (Event event : currentEvents) {
//            if (!event.hasEnded())
//                event.tick();
//        }
//
//        Variables.timeSinceLastRespawn++;
//        eventCountDown--;
//    }
//
//    public void endChaos(){
//        System.out.println("Ending Chaos");
//        if(client.player !=null)
//            currentEvents.forEach(Event::end);
//    }
//
//    private Event getRandomEvent(boolean isVoting){
//        //return new EnemyRaidsEvent();
//        return EventRegistry.getRandomDifferentEvent(isVoting? nextEvents:currentEvents,isVoting).getLeft();
//    }

}
