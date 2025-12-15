export class HttpError extends Error {
  status: number;
  body?: any;

  constructor(status: number, message?: string, body?: any) {
    super(message ?? "HTTP Error");
    this.name = "HttpError";
    this.status = status;
    this.body = body;
  }
}

export default HttpError;
