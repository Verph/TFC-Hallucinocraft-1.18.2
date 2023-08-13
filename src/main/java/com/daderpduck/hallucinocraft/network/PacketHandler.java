package com.daderpduck.hallucinocraft.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableInt;

import net.dries007.tfc.network.DataManagerSyncPacket;
import net.dries007.tfc.util.DataManager;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.capabilities.DrugCapability;
import com.daderpduck.hallucinocraft.util.HCHelpers;

public class PacketHandler
{
    private static final String VERSION = Hallucinocraft.MOD_VERSION;
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(HCHelpers.identifier("network"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static final MutableInt ID = new MutableInt(0);

    public static void send(PacketDistributor.PacketTarget target, Object message)
    {
        CHANNEL.send(target, message);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Hallucinocraft.MOD_ID, "messages"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void init()
    {
        register(DrugCapSync.class, DrugCapSync::new);
        register(ActiveDrugCapSync.class, ActiveDrugCapSync::new);

        registerDataManager(DrugCapability.Packet.class, DrugCapability.MANAGER);
    }

    private static int id = 0;
    private static <MSG extends IMessage> void register(Class<MSG> msg, Function<FriendlyByteBuf, MSG> decoder)
    {
        INSTANCE.registerMessage(id++, msg, IMessage::encode, decoder, IMessage::handle);
    }

    @SuppressWarnings("unchecked")
    public static <T extends DataManagerSyncPacket<E>, E> void registerDataManager(Class<T> cls, DataManager<E> manager, SimpleChannel channel, int id)
    {
        channel.registerMessage(id, cls,
            (packet, buffer) -> packet.encode(manager, buffer),
            buffer -> {
                final T packet = (T) manager.createEmptyPacket();
                packet.decode(manager, buffer);
                return packet;
            },
            (packet, context) -> {
                context.get().setPacketHandled(true);
                context.get().enqueueWork(() -> packet.handle(context.get(), manager));
            });
    }

    private static <T extends DataManagerSyncPacket<E>, E> void registerDataManager(Class<T> cls, DataManager<E> manager)
    {
        registerDataManager(cls, manager, CHANNEL, ID.getAndIncrement());
    }

    private static <T> void register(Class<T> cls, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, NetworkEvent.Context> handler)
    {
        CHANNEL.registerMessage(ID.getAndIncrement(), cls, encoder, decoder, (packet, context) -> {
            context.get().setPacketHandled(true);
            handler.accept(packet, context.get());
        });
    }

    private static <T> void register(Class<T> cls, Supplier<T> factory, BiConsumer<T, NetworkEvent.Context> handler)
    {
        CHANNEL.registerMessage(ID.getAndIncrement(), cls, (packet, buffer) -> {}, buffer -> factory.get(), (packet, context) -> {
            context.get().setPacketHandled(true);
            handler.accept(packet, context.get());
        });
    }
}
