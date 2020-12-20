# xml-structure-editor

Example of using and comparison of simple replacement and StAX if you need to perform some operations on XML structure (like tags renaming of removing, adding new tags or attributes and so on).

### Example
Initial XML document looks like:
```xml
<?xml version="1.0" ?>
<!-- This is a comment -->
<note>
    <to>Me</to>
    <from>You</from>
    <heading>Reminder</heading>
    <body>Hello!</body>
</note>
```

Task is
- to remove all comments
- to rename note `tag` wih `reminder`
- to remove `heading` tag
- to add `time` tag inside of `reminder`

So, the final XML will be:
```xml
<?xml version="1.0" ?>
<reminder>
    <time>Mon Dec 21 00:03:21 MSK 2020</time>
    <to>Me</to>
    <from>You</from>
    <body>Hello!</body>
</reminder>
```
### Using String.replaceAll
Your can see the example in `ru.drmwks.ReplaceExample.processData`
In this example we only use simple replacement with regex.

#### Advantages
- Simple code

#### Disadvantages
- No XML structure validation
- High probability of errors
- String allocation for every `replaceAll` call
- Better use only in small and non-critical XML

### Using StAX
Your can see the example in `ru.drmwks.StaxExample.processData`
In this example we use all power of streaming API: we read data from input and write the result XML on-fly using `XMLStreamReader` and `XMLStreamWriter`

#### Advantages
- XML structure validation
- Could be extended for all node types
- Can be used for SOAP integration proxies (when you need to transform data structure on-fly)

#### Disadvantages
- More code