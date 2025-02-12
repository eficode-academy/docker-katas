FROM golang:1.13.2 AS builder
WORKDIR /go
COPY hello.go /go
RUN go build hello.go

FROM scratch
COPY --from=builder /go/hello /hello
CMD ["/hello"]
