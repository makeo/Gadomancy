package makeo.gadomancy.client.fx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FXLightningBoltCommon
{
    ArrayList segments;
    Vector3 start;
    Vector3 end;
    HashMap splitparents;
    public float multiplier;
    public double length;
    public int numsegments0;
    public int increment;

    public class BoltPoint
    {
        Vector3 point;
        Vector3 basepoint;
        Vector3 offsetvec;
        final FXLightningBoltCommon this$0;

        public BoltPoint(Vector3 basepoint, Vector3 offsetvec)
        {
            this.this$0 = FXLightningBoltCommon.this;
            this.point = basepoint.clone().add(offsetvec);
            this.basepoint = basepoint;
            this.offsetvec = offsetvec;
        }
    }

    public class Segment
    {
        public FXLightningBoltCommon.BoltPoint startpoint;
        public FXLightningBoltCommon.BoltPoint endpoint;
        public Vector3 diff;
        public Segment prev;
        public Segment next;
        public Vector3 nextdiff;
        public Vector3 prevdiff;
        public float sinprev;
        public float sinnext;
        public float light;
        public int segmentno;
        public int splitno;
        final FXLightningBoltCommon this$0;

        public void calcDiff()
        {
            this.diff = this.endpoint.point.clone().subtract(this.startpoint.point);
        }

        public void calcEndDiffs()
        {
            if (this.prev != null)
            {
                Vector3 prevdiffnorm = this.prev.diff.clone().normalize();
                Vector3 thisdiffnorm = this.diff.clone().normalize();
                this.prevdiff = thisdiffnorm.add(prevdiffnorm).normalize();
                this.sinprev = ((float)Math.sin(Vector3.anglePreNorm(thisdiffnorm, prevdiffnorm.multiply(-1.0F)) / 2.0F));
            }
            else
            {
                this.prevdiff = this.diff.clone().normalize();
                this.sinprev = 1.0F;
            }
            if (this.next != null)
            {
                Vector3 nextdiffnorm = this.next.diff.clone().normalize();
                Vector3 thisdiffnorm = this.diff.clone().normalize();
                this.nextdiff = thisdiffnorm.add(nextdiffnorm).normalize();
                this.sinnext = ((float)Math.sin(Vector3.anglePreNorm(thisdiffnorm, nextdiffnorm.multiply(-1.0F)) / 2.0F));
            }
            else
            {
                this.nextdiff = this.diff.clone().normalize();
                this.sinnext = 1.0F;
            }
        }

        public String toString()
        {
            return this.startpoint.point.toString() + " " + this.endpoint.point.toString();
        }

        public Segment(FXLightningBoltCommon.BoltPoint start, FXLightningBoltCommon.BoltPoint end, float light, int segmentnumber, int splitnumber)
        {
            this.this$0 = FXLightningBoltCommon.this;
            this.startpoint = start;
            this.endpoint = end;
            this.light = light;
            this.segmentno = segmentnumber;
            this.splitno = splitnumber;
            calcDiff();
        }

        public Segment(Vector3 start, Vector3 end)
        {
            this(new FXLightningBoltCommon.BoltPoint(start, new Vector3(0.0D, 0.0D, 0.0D)), new FXLightningBoltCommon.BoltPoint(end, new Vector3(0.0D, 0.0D, 0.0D)), 1.0F, 0, 0);
        }
    }

    public class SegmentLightSorter
            implements Comparator
    {
        final FXLightningBoltCommon this$0;

        public int compare(FXLightningBoltCommon.Segment o1, FXLightningBoltCommon.Segment o2)
        {
            return Float.compare(o2.light, o1.light);
        }

        public int compare(Object obj, Object obj1)
        {
            return compare((FXLightningBoltCommon.Segment)obj, (FXLightningBoltCommon.Segment)obj1);
        }

        public SegmentLightSorter()
        {
            this.this$0 = FXLightningBoltCommon.this;
        }
    }

    public class SegmentSorter
            implements Comparator
    {
        final FXLightningBoltCommon this$0;

        public int compare(FXLightningBoltCommon.Segment o1, FXLightningBoltCommon.Segment o2)
        {
            int comp = Integer.valueOf(o1.splitno).compareTo(Integer.valueOf(o2.splitno));
            if (comp == 0) {
                return Integer.valueOf(o1.segmentno).compareTo(Integer.valueOf(o2.segmentno));
            }
            return comp;
        }

        public int compare(Object obj, Object obj1)
        {
            return compare((FXLightningBoltCommon.Segment)obj, (FXLightningBoltCommon.Segment)obj1);
        }

        public SegmentSorter()
        {
            this.this$0 = FXLightningBoltCommon.this;
        }
    }

    public FXLightningBoltCommon(World world, Vector3 jammervec, Vector3 targetvec, long seed)
    {
        this.segments = new ArrayList();
        this.splitparents = new HashMap();
        this.world = world;
        this.start = jammervec;
        this.end = targetvec;
        this.seed = seed;
        this.rand = new Random(seed);
        this.numsegments0 = 1;
        this.increment = 1;
        this.length = this.end.clone().subtract(this.start).length();
        this.particleMaxAge = (3 + this.rand.nextInt(3) - 1);
        this.multiplier = 1.0F;
        this.particleAge = (-(int)(this.length * 3.0F));





        this.segments.add(new Segment(this.start, this.end));
    }

    public FXLightningBoltCommon(World world, Entity detonator, Entity target, long seed)
    {
        this(world, new Vector3(detonator.getPosition()), new Vector3(target.getPosition()), seed);
    }

    public FXLightningBoltCommon(World world, Entity detonator, Entity target, long seed, int speed)
    {
        this(world, new Vector3(detonator.getPosition()), new Vector3(target.posX, target.posY + target.getEyeHeight() - 0.699999988079071D, target.posZ), seed);
        this.increment = speed;
        this.multiplier = 0.4F;
    }

    public FXLightningBoltCommon(World world, TileEntity detonator, Entity target, long seed)
    {
        this(world, new Vector3(detonator.getPos()), new Vector3(target.getPosition()), seed);
    }

    public FXLightningBoltCommon(World world, TileEntity detonator, double x, double y, double z, long seed)
    {
        this(world, new Vector3(detonator.getPos()), new Vector3(x, y, z), seed);
    }

    public FXLightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi)
    {
        this(world, new Vector3(x1, y1, z1), new Vector3(x, y, z), seed);
        this.particleMaxAge = (duration + this.rand.nextInt(duration) - duration / 2);
        this.multiplier = multi;
    }

    public FXLightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed)
    {
        this(world, new Vector3(x1, y1, z1), new Vector3(x, y, z), seed);
        this.particleMaxAge = (duration + this.rand.nextInt(duration) - duration / 2);
        this.multiplier = multi;
        this.increment = speed;
    }

    public void setMultiplier(float m)
    {
        this.multiplier = m;
    }

    public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle)
    {
        if (this.finalized) {
            return;
        }
        ArrayList oldsegments = this.segments;
        this.segments = new ArrayList();
        Segment prev = null;
        for (Iterator iterator = oldsegments.iterator(); iterator.hasNext();)
        {
            Segment segment = (Segment)iterator.next();
            prev = segment.prev;
            Vector3 subsegment = segment.diff.clone().multiply(1.0F / splits);
            BoltPoint[] newpoints = new BoltPoint[splits + 1];
            Vector3 startpoint = segment.startpoint.point;
            newpoints[0] = segment.startpoint;
            newpoints[splits] = segment.endpoint;
            for (int i = 1; i < splits; i++)
            {
                Vector3 randoff = Vector3.getPerpendicular(segment.diff).rotate(this.rand.nextFloat() * 360.0F, segment.diff);
                randoff.multiply((this.rand.nextFloat() - 0.5F) * amount);
                Vector3 basepoint = startpoint.clone().add(subsegment.clone().multiply(i));
                newpoints[i] = new BoltPoint(basepoint, randoff);
            }
            for (int i = 0; i < splits; i++)
            {
                Segment next = new Segment(newpoints[i], newpoints[(i + 1)], segment.light, segment.segmentno * splits + i, segment.splitno);
                next.prev = prev;
                if (prev != null) {
                    prev.next = next;
                }
                if ((i != 0) && (this.rand.nextFloat() < splitchance))
                {
                    Vector3 splitrot = Vector3.xCrossProduct(next.diff).rotate(this.rand.nextFloat() * 360.0F, next.diff);
                    Vector3 diff = next.diff.clone().rotate((this.rand.nextFloat() * 0.66F + 0.33F) * splitangle, splitrot).multiply(splitlength);
                    this.numsplits += 1;
                    this.splitparents.put(this.numsplits, next.splitno);
                    Segment split = new Segment(newpoints[i], new BoltPoint(newpoints[(i + 1)].basepoint, newpoints[(i + 1)].offsetvec.clone().add(diff)), segment.light / 2.0F, next.segmentno, this.numsplits);
                    split.prev = prev;
                    this.segments.add(split);
                }
                prev = next;
                this.segments.add(next);
            }
            if (segment.next != null) {
                segment.next.prev = prev;
            }
        }
        this.numsegments0 *= splits;
    }

    public void defaultFractal()
    {
        fractal(2, (float) (this.length * this.multiplier / 8.0F), 0.7F, 0.1F, 45.0F);
        fractal(2, (float) (this.length * this.multiplier / 12.0F), 0.5F, 0.1F, 50.0F);
        fractal(2, (float) (this.length * this.multiplier / 17.0F), 0.5F, 0.1F, 55.0F);
        fractal(2, (float) (this.length * this.multiplier / 23.0F), 0.5F, 0.1F, 60.0F);
        fractal(2, (float) (this.length * this.multiplier / 30.0F), 0.0F, 0.0F, 0.0F);
        fractal(2, (float) (this.length * this.multiplier / 34.0F), 0.0F, 0.0F, 0.0F);
        fractal(2, (float) (this.length * this.multiplier / 40.0F), 0.0F, 0.0F, 0.0F);
    }

    private void calculateCollisionAndDiffs()
    {
        HashMap<Integer, Integer> lastactivesegment = new HashMap<Integer, Integer> ();
        Collections.sort(this.segments, new SegmentSorter());
        int lastsplitcalc = 0;
        int lastactiveseg = 0;
        for (Iterator iterator = this.segments.iterator(); iterator.hasNext();)
        {
            Segment segment = (Segment)iterator.next();
            if (segment.splitno > lastsplitcalc)
            {
                lastactivesegment.put(lastsplitcalc, lastactiveseg);
                lastsplitcalc = segment.splitno;
                lastactiveseg = lastactivesegment.get(this.splitparents.get(Integer.valueOf(segment.splitno)));
            }
            lastactiveseg = segment.segmentno;
        }
        lastactivesegment.put(lastsplitcalc, lastactiveseg);
        lastsplitcalc = 0;
        lastactiveseg = lastactivesegment.get(Integer.valueOf(0));
        Segment segment;
        for (Iterator iterator = this.segments.iterator(); iterator.hasNext(); segment.calcEndDiffs())
        {
            segment = (Segment)iterator.next();
            if (lastsplitcalc != segment.splitno)
            {
                lastsplitcalc = segment.splitno;
                lastactiveseg = lastactivesegment.get(Integer.valueOf(segment.splitno));
            }
            if (segment.segmentno > lastactiveseg) {
                iterator.remove();
            }
        }
    }

    public void finalizeBolt()
    {
        if (this.finalized) {
            return;
        }
        this.finalized = true;
        calculateCollisionAndDiffs();
        Collections.sort(this.segments, new SegmentLightSorter());
    }

    public void onUpdate()
    {
        this.particleAge += this.increment;
        if (this.particleAge > this.particleMaxAge) {
            this.particleAge = this.particleMaxAge;
        }
    }

    public int type = 0;
    public boolean nonLethal = false;
    private int numsplits;
    private boolean finalized;
    private Random rand;
    public long seed;
    public int particleAge;
    public int particleMaxAge;
    private World world;
    public static final float speed = 3.0F;
    public static final int fadetime = 20;
}