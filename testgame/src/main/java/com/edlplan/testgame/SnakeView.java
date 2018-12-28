package com.edlplan.testgame;

import android.graphics.Bitmap;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.line.AbstractPath;
import com.edplan.framework.graphics.line.LegacyDrawLinePath;
import com.edplan.framework.graphics.line.LinePath;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.batch.Texture3DBatch;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.math.MathExt;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec2Int;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class SnakeView extends EdView {

    public static final int GAME_STATE_PAUSE = 1;

    public static final int GAME_STATE_END = 2;

    public static final int GAME_STATE_RUN = 3;

    private Map map;

    private Snake snake;

    private Item item;

    private double time;

    private double timePerStep = 300;

    private int gameState = GAME_STATE_PAUSE;


    public SnakeView(MContext context) {
        super(context);
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        map = new Map(getContext());
        snake = new Snake(getContext(), map, map.getWidth() / 2, map.getHeight() / 2, 5);
        item = new Item(getContext(), map, map.toMap(new Vec2Int(map.getWidth() / 2 + 3, map.getHeight() / 2)));
        post(() -> setGameState(GAME_STATE_RUN), 2000);
    }

    @Override
    public void onFrameStart() {
        super.onFrameStart();
        if (gameState == GAME_STATE_PAUSE || gameState == GAME_STATE_END) {
            return;
        }
        time += getContext().getFrameDeltaTime();
        while (time > timePerStep) {
            time -= timePerStep;
            if (!snake.advanceStep()) {
                gameState = GAME_STATE_END;
                return;
            } else {
                item.onSnakeUpdate(snake, snake.getHead());
            }
        }
        snake.setOffset((float) (time / timePerStep));
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
        map.draw(canvas);
        snake.draw(canvas);
        item.draw(canvas);
    }

    public Snake getSnake() {
        return snake;
    }

    public static class Map extends EdDrawable{

        private float savedCanvasWidth = 0;

        private int width = 20;

        private int height = 20;

        public Map(MContext context) {
            super(context);
        }

        /**
         * 转换到当前地图位置
         * @param vec2Int 原始位置
         * @return 地图上的有效位置
         */
        public Vec2Int toMap(Vec2Int vec2Int) {
            return new Vec2Int(MathExt.floorMod(vec2Int.x, width), MathExt.floorMod(vec2Int.y, height));
        }

        /**
         * @param x 点的x坐标
         * @param y 点的y坐标
         * @return 这个位置是否是空的
         */
        public boolean isEmpty(int x, int y) {
            return true;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public float getTileSize() {
            return savedCanvasWidth / width;
        }

        @Override
        public void draw(BaseCanvas canvas) {
            savedCanvasWidth = canvas.getWidth();
            canvas.drawTexture(
                    GLTexture.Black,
                    RectF.ltrb(0, 0, canvas.getWidth(), canvas.getHeight()),
                    Color4.ONE, 1);
        }
    }

    public static class Item extends EdDrawable{

        ColorRectSprite sprite;

        Vec2Int position;

        Map map;

        Random random = new Random();

        public Item(MContext context, Map map, Vec2Int position) {
            super(context);
            this.map = map;
            this.position = position;

            sprite = new ColorRectSprite(context);
            sprite.setAccentColor(Color4.Green);
        }

        public void randomPosition(Snake snake) {
            //留个bug没人会发现吧
            while (true) {
                position = map.toMap(new Vec2Int(random.nextInt(map.getWidth()), random.nextInt(map.getHeight())));
                if (map.isEmpty(position.x, position.y) && !snake.isBody(position.x, position.y)) {
                    break;
                }
            }
        }

        public void onSnakeUpdate(Snake snake, Vec2Int position) {
            if (this.position.equals(position)) {
                snake.grow();
                randomPosition(snake);
            }
        }

        @Override
        public void draw(BaseCanvas canvas) {
            sprite.setArea(RectF.anchorOWH(
                    Anchor.Center,
                    map.getTileSize() * (position.x + 0.5f),
                    map.getTileSize() * (position.y + 0.5f),
                    map.getTileSize()*0.6f,
                    map.getTileSize()*0.6f
            ));
            sprite.draw(canvas);
        }
    }

    public static class Snake extends EdDrawable{

        private LinkedList<Vec2Int> body = new LinkedList<>();

        private Map map;

        private int zipedSteps = 0;

        private Direction direction = Direction.Left;

        private GLTexture bodyTexture;

        private float offset;

        public Snake(MContext context, Map map, int x, int y, int initialLength) {
            super(context);
            this.map = map;
            if (initialLength < 1) {
                initialLength = 1;
            }
            body.add(new Vec2Int(x, y));
            zipedSteps = initialLength - 1;
        }

        public void setOffset(float offset) {
            this.offset = offset;
        }

        public boolean isBody(int x, int y) {
            return body.contains(new Vec2Int(x, y));
        }

        public Vec2Int getHead() {
            return body.getLast();
        }

        public int length() {
            return zipedSteps + body.size();
        }

        public void grow() {
            zipedSteps++;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        /**
         * 前进一格
         * @return 可用前进则返回true，无法前进则返回false
         */
        public boolean advanceStep() {
            Vec2Int next = map.toMap(body.getLast().copy().add(direction.dx, direction.dy));
            if (isBody(next.x, next.y)) {
                return false;
            }
            if (!map.isEmpty(next.x, next.y)) {
                return false;
            }
            body.addLast(next);
            if (zipedSteps > 0) {
                zipedSteps--;
            } else {
                body.removeFirst();
            }
            return true;
        }

        private ArrayList<AbstractPath> createBodySlider() {
            ArrayList<AbstractPath> paths = new ArrayList<>();
            LinePath path = new LinePath();
            path.setWidth(map.getTileSize() / 2 * 0.9f);
            paths.add(path);
            Vec2Int pre = null;
            for (Vec2Int v : body) {
                if (pre != null && (Math.abs(v.x - pre.x) + Math.abs(v.y - pre.y)) != 1) {
                    path = new LinePath();
                    path.setWidth(map.getTileSize() / 2 * 0.9f);
                    paths.add(path);
                }
                path.add(new Vec2(map.getTileSize() * (v.x + 0.5f), map.getTileSize() * (v.y + 0.5f)));
                pre = v;
            }

            LinePath last = (LinePath) paths.get(paths.size() - 1);
            Vec2 head = last.getLast();
            last.add(head.copy().add(direction.dx * offset * map.getTileSize(), direction.dy * offset * map.getTileSize()));

            return paths;
        }

        private void drawBody(BaseCanvas canvas) {
            if (bodyTexture == null) {
                updateTexture();
            }
            ArrayList<AbstractPath> paths = createBodySlider();
            Texture3DBatch<TextureVertex3D> batch = new Texture3DBatch<>();
            GLWrapped.depthTest.save();
            GLWrapped.depthTest.forceSet(true);
            canvas.getBlendSetting().save();
            canvas.getBlendSetting().setEnable(false);
            for (AbstractPath path : paths) {
                LegacyDrawLinePath<Texture3DBatch> d = new LegacyDrawLinePath<>(path);
                batch.clear();
                d.addToBatch(batch);
                canvas.drawTexture3DBatch(batch, bodyTexture, 1, Color4.White);
            }
            canvas.getBlendSetting().restore();
            GLWrapped.depthTest.restore();
        }

        @Override
        public void draw(BaseCanvas canvas) {
            drawBody(canvas);
        }

        private void updateTexture() {
            float aa_portion = 0.02f;
            float border_portion = 0.128f;
            float mix_width = 0.02f;
            float mix_start = border_portion - mix_width;
            float gradient_portion = 1 - border_portion + mix_width;

            Color4 centerColor = Color4.rgba(0.8f, 0.8f, 0.8f, 0.7f);
            Color4 borderColor = Color4.rgba(1, 1, 1, 1);
            Color4 sideColor = Color4.rgba(0.25f, 0.25f, 0.25f, 0.7f);

            Bitmap bmp = Bitmap.createBitmap(512, 1, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < bmp.getWidth(); x++) {
                float v = 1 - x / (float) (bmp.getWidth() - 1);

                if (v <= mix_start) {
                    bmp.setPixel(x, 0,
                            borderColor
                                    .copyNew()
                                    .multiple(
                                            Color4.alphaMultipler(
                                                    Math.min(v / aa_portion, 1)
                                            )
                                    ).toIntBit());
                } else if (v <= border_portion) {
                    Color4 c0 = borderColor.copyNew();
                    float mix_rate = (v - mix_start) / mix_width;
                    v -= border_portion;
                    Color4 c1 = Color4.mix(centerColor, sideColor, 1 - v / gradient_portion);
                    bmp.setPixel(x, 0, Color4.mix(c0, c1, mix_rate).toIntBit());
                } else {
                    v -= border_portion;
                    bmp.setPixel(x, 0, Color4.mix(centerColor, sideColor, 1 - v / gradient_portion).toIntBit());
                }
            }

            if (bodyTexture != null) bodyTexture.delete();
            bodyTexture = GLTexture.create(bmp);
        }




        public enum Direction {
            Up(0, -1),
            Down(0, 1),
            Left(-1, 0),
            Right(1, 0),;
            final int dx,dy;

            Direction(int dx, int dy) {
                this.dx = dx;
                this.dy = dy;
            }
        }

    }

}
