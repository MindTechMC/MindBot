package club.mindtech.mindbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Locale;

public class CommandHelp extends BaseCommand {

    public CommandHelp() {
        super("help", "Shows information about other commands", "help [command]");
    }

    @Override
    public CommandData getCommandData() {
        CommandData data = super.getCommandData();
        data.addOption(OptionType.STRING, "command", "Command name the help should be shown for", false);
        return data;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        super.onSlashCommand(event);

        BaseCommand embedFor = this;
        OptionMapping mapping = event.getOption("command");

        if (mapping != null) {
            String commandName = lower(mapping.getAsString());
            if (Commands.isCommand(commandName)) {
                embedFor = Commands.getCommand(commandName);
            }
        }

        event.deferReply()
             .addEmbeds(getHelpEmbed(event.getUser(), embedFor))
             .queue();
    }

    private static MessageEmbed getHelpEmbed(User author, BaseCommand command) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Command: " + command.getName())
                .setDescription(command.getDescription())
                .addField("Usage:", command.getUsage(), true)
                .setColor(0x1dd1a1)
                .setFooter("Requested by " + author.getName());

        return embed.build();
    }

    private static String lower(String s) {
        return s.toLowerCase(Locale.ROOT);
    }
}
