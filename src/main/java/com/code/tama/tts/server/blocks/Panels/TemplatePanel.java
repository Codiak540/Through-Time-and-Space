/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.blocks.Panels;

import com.code.tama.tts.client.TTSSounds;
import com.code.tama.tts.server.blocks.VoxelRotatedShape;
import com.code.tama.tts.server.capabilities.CapabilityConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class TemplatePanel extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty PRESSED_BUTTON = IntegerProperty.create("pressed_button", 0, 3);
    public static VoxelRotatedShape SHAPE =
            new VoxelRotatedShape(createVoxelShape().optimize());
    public static List<Buttons> buttons = new ArrayList<>();

    public static VoxelShape createVoxelShape() {
        return Stream.of(
                        Block.box(11, 0.5, 3, 14, 1.5, 6),
                        Block.box(6.5, 0.5, 3, 9.5, 1.5, 6),
                        Block.box(2, 0.5, 3, 5, 1.5, 6),
                        Block.box(0, 0, 0, 16, 1, 16),
                        Block.box(5.5, 1, 8, 10.5, 3.5, 13))
                .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR))
                .get();
    }

    public TemplatePanel(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(
                this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(PRESSED_BUTTON, 0));
    }

    public Buttons getButton(double mouseX, double mouseZ, Direction facing) {

        for (Buttons button : buttons) {
            if (button.values.containsKey(facing)) {
                Vec2 vec = button.values.get(facing);
                float width = button.width;
                float height = button.height;
                float x = vec.x;
                float z = vec.y;
                switch (facing) {
                    case EAST:
                        if (mouseX >= (double) x
                                && mouseX <= (double) (x + height)
                                && mouseZ <= (double) z
                                && mouseZ >= (double) (z - width)) {
                            return button;
                        }
                        break;

                    case SOUTH:
                        if (mouseX >= (double) x
                                && mouseZ >= (double) z
                                && mouseX <= (double) (x + width)
                                && mouseZ <= (double) (z + height)) {
                            return button;
                        }
                        break;

                    case WEST:
                        if (mouseX <= (double) x
                                && mouseX >= (double) (x - height)
                                && mouseZ >= (double) z
                                && mouseZ <= (double) (z + width)) {
                            return button;
                        }
                        break;

                    default:
                        if (mouseX <= (double) x
                                && mouseZ <= (double) z
                                && mouseX >= (double) (x - width)
                                && mouseZ >= (double) (z - height)) {
                            return button;
                        }
                }
            }
        }

        return Buttons.EMPTY;
    }

    @Override
    public @NotNull VoxelShape getShape(
            BlockState p_60555_,
            @NotNull BlockGetter p_60556_,
            @NotNull BlockPos p_60557_,
            @NotNull CollisionContext p_60558_) {
        return SHAPE.GetShapeFromRotation(p_60555_.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(PRESSED_BUTTON, 0);
    }

    @Override
    public @NotNull BlockState mirror(BlockState p_54122_, Mirror p_54123_) {
        return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState rotate(BlockState p_54125_, Rotation p_54126_) {
        return p_54125_.setValue(FACING, p_54126_.rotate(p_54125_.getValue(FACING)));
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.is(this); // Avoids rendering internal faces
    }

    public void tick(
            BlockState state,
            @NotNull ServerLevel serverLevel,
            @NotNull BlockPos pos,
            @NotNull RandomSource randomSource) {
        if (state.getValue(PRESSED_BUTTON) != 0) {
            serverLevel.setBlock(pos, state.setValue(PRESSED_BUTTON, 0), 3);
        }
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            Level world,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            BlockHitResult hit) {
        // if (world.isClientSide) return InteractionResult.PASS;
        if (hand.equals(InteractionHand.OFF_HAND)) return InteractionResult.PASS;

        Buttons button = this.getButton(
                (100.0F * (float) (hit.getLocation().x() - (double) pos.getX())) / 100.0F,
                (100.0F * (float) (hit.getLocation().z() - (double) pos.getZ())) / 100.0F,
                state.getValue(FACING).getOpposite());

        if (button == null) return InteractionResult.FAIL;

        world.getCapability(CapabilityConstants.TARDIS_LEVEL_CAPABILITY).ifPresent(tardisLevelCapability -> {
            switch (button) {
                case ONE:
                    world.setBlock(pos, state.setValue(PRESSED_BUTTON, 1), 3);
                    world.scheduleTick(pos, this, 10);
                    world.playSound(null, pos, TTSSounds.BUTTON_CLICK_01.get(), SoundSource.BLOCKS);
                    break;
                case TWO:
                    world.setBlock(pos, state.setValue(PRESSED_BUTTON, 2), 3);
                    world.scheduleTick(pos, this, 10);
                    world.playSound(null, pos, TTSSounds.BUTTON_CLICK_01.get(), SoundSource.BLOCKS);
                    break;
                case THREE:
                    world.setBlock(pos, state.setValue(PRESSED_BUTTON, 3), 3);
                    world.scheduleTick(pos, this, 10);
                    world.playSound(null, pos, TTSSounds.BUTTON_CLICK_01.get(), SoundSource.BLOCKS);
                    break;
                default:
                    break;
            }
        });

        // }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> StateDefinition) {
        super.createBlockStateDefinition(StateDefinition);
        StateDefinition.add(FACING);
        StateDefinition.add(PRESSED_BUTTON);
    }

    public enum Buttons {
        EMPTY(null, 0.0F, 0.0F, 0.0F, 0.0F),
        ONE("ONE", 3.00f, 3.00f, 11.00f, 3.00f),
        THREE("THREE", 3.00f, 3.00f, 2.00f, 3.00f),
        TWO("TWO", 3.00f, 3.00f, 6.50f, 3.00f);

        Component displayName;
        final float height;
        final Map<Direction, Vec2> values = new HashMap<>();
        final float width;

        Buttons(String s, float w, float h, float x1, float z1) {
            float f = 0.0625F;
            this.width = w * f;
            this.height = h * f;
            float x2 = 16.0F - x1;
            float z2 = 16.0F - z1;
            this.values.put(Direction.NORTH, new Vec2(x2 * f, z2 * f));
            this.values.put(Direction.EAST, new Vec2(z1 * f, x2 * f));
            this.values.put(Direction.SOUTH, new Vec2(x1 * f, z1 * f));
            this.values.put(Direction.WEST, new Vec2(z2 * f, x1 * f));
            if (s != null) {
                this.displayName = Component.literal(s);
            }
            TemplatePanel.buttons.add(this);
        }
    }
}
