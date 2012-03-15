package ro.koch.kolabrestapi;

public class PaginationRange {
    public final int offset;
    public final int limit;

    public PaginationRange(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public boolean moreToCome(int size) {
        return size > offset + limit;
    }

    public int nextOffset() {
        return offset + limit;
    }
}
