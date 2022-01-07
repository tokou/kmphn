import Hackernews

func valueOf<T: AnyObject>(_ value: T) -> DecomposeValue<T> {
    return MutableValueBuilderKt.MutableValue(initialValue: value) as! MutableValue<T>
}
